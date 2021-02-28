package org.sfmis.coactivity.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.LocalActivity;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LocalActivityService extends AutoService {
	@Autowired
	DB db;

	@Autowired
	Authenticated auth;

	@Autowired
	ValidationService validationService;

	public Map<String, String> rules() {
		Map<String, String> rules = new HashMap<>();
		rules.put("code", "required");
		return rules;

	}

	public Map<String, Object> index() {
		int perPage = (request("rows") == null | (request("rows")).isBlank()) ? 10 : Integer.parseInt(request("rows"));
		int page = (request("page") == null | (request("page")).isBlank()) ? 1 : Integer.parseInt(request("page"));
		if (perPage > 100) {
			perPage = 100;
		}
		String sql = "select (select code,nameNp,nameEn,disabled,approved from  coactivity.localActivity order by code ASC offset "
				+ ((page - 1) * perPage) + " rows fetch next " + perPage + " rows only for json auto) as rows";

		Map<String, Object> result = new HashMap<>();
		List<Tuple> rsTuple = db.getResultList(sql);
		System.out.println(rsTuple.get(0).get(0));
		result.put("rows", rsTuple.get(0).get(0));
		result.put("currentPage", page);
		result.put("perPage", perPage);
		sql = "select count(laId) from coactivity.localActivity ";
		Tuple totalRows = db.getSingleResult(sql);
		result.put("total", totalRows.get(0));
		return Messenger.getMessenger().setData(result).success();
	}

	public Map<String, Object> store() {
		LocalActivity model = new LocalActivity();
		model.loadData(document);
		String sql = "INSERT INTO coactivity.localActivity(laId, activityId, adminId, lmOrgId, code, nameNp, nameEn, disabled, enterBy, enteryDate, approved) VALUES (dbo.newidint(),?,?,?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.activityId, model.adminId, model.lmOrgId, model.code,
				model.nameNp, model.nameEn, model.disabled, auth.getUserId(), model.approved));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public Map<String, Object> edit(String id) {
		String sql = "SELECT laId, activityId, adminId, lmOrgId, code, nameNp, nameEn, disabled, enterBy, enteryDate, approved from coactivity.localActivity where laId=? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}

	}

	public Map<String, Object> update(String id) {
		LocalActivity model = new LocalActivity();
		model.loadData(document);
		String sql = "UPDATE coactivity.localActivity set activityId =?, adminId=?, lmOrgId =?, code=?, nameNp=?, nameEn=?, disabled=?,approved=? where laId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.activityId, model.adminId, model.lmOrgId, model.code,
				model.nameNp, model.nameEn, model.disabled, model.approved, id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public Map<String, Object> destroy(String id) {
		String sql = "DELETE from coactivity.localActivity where  laId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}
	}
}
