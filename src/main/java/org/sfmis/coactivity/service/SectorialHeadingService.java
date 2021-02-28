package org.sfmis.coactivity.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.SectorialHeading;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectorialHeadingService extends AutoService {
	@Autowired
	DB db;

	@Autowired
	Authenticated auth;

	@Autowired
	ValidationService validationService;

	public Map<String, Object> index() {
		int perPage = (request("rows") == null | (request("rows")).isBlank()) ? 10 : Integer.parseInt(request("rows"));
		int page = (request("page") == null | (request("page")).isBlank()) ? 1 : Integer.parseInt(request("page"));
		if (perPage > 100) {
			perPage = 100;
		}
		String sql = "select (select code,nameNp,nameEn,disabled,approved from  coactivity.sectorialHeading order by code ASC offset "
				+ ((page - 1) * perPage) + " rows fetch next " + perPage + " rows only for json auto) as rows";

		Map<String, Object> result = new HashMap<>();
		List<Tuple> rsTuple = db.getResultList(sql);
		System.out.println(rsTuple.get(0).get(0));
		result.put("rows", rsTuple.get(0).get(0));
		result.put("currentPage", page);
		result.put("perPage", perPage);
		sql = "select count(id) from coactivity.sectorialHeading ";
		Tuple totalRows = db.getSingleResult(sql);
		result.put("total", totalRows.get(0));
		return Messenger.getMessenger().setData(result).success();
	}

	public Map<String, Object> store() {
		SectorialHeading model = new SectorialHeading();
		model.loadData(document);
		String sql = "INSERT INTO coactivity.sectorialHeading(id, adminLevel, adminId, sectorId, code, parentId, nameNp,nameEn, keys, keyNumber, disabled, enterby, entrydate, approved ) VALUES(dbo.newidint(),?,?,?,?,?,?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql,
				Arrays.asList(model.adminLevel, model.adminId, model.sectorId, model.code, model.parentId, model.nameNp,
						model.nameEn, model.keys, model.keyNumber, model.disabled, auth.getUserId(), model.approved));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public Map<String, Object> edit(String id) {
		String sql = "SELECT id, adminLevel, adminId, sectorId, code, parentId, nameNp,nameEn, keys, keyNumber, disabled, enterby, entrydate, approved from coactivity.sectorialHeading where  id =? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}

	}

	public Map<String, Object> update(String id) {
		SectorialHeading model = new SectorialHeading();
		model.loadData(document);
		String sql = "UPDATE coactivity.sectorialHeading set adminLevel=?, adminId=?, sectorId=?, code=?, parentId=?, nameNp=?,nameEn=?, keys=?, keyNumber=?, disabled=?, approved=? where id=?";
		int rowEffect = db.executeUpdate(sql,
				Arrays.asList(model.adminLevel, model.adminId, model.sectorId, model.code, model.parentId, model.nameNp,
						model.nameEn, model.keys, model.keyNumber, model.disabled, model.approved, id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}
	}

	public Map<String, Object> destroy(String id) {
		String sql = "DELETE from coactivity.sectorialHeading where id=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}
	}

	public List<Map<String, String>> getActivities() {
		return null;
	}
}
