package org.sfmis.coactivity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.CentralActivity;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.DbResponse;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CentralActivityService extends AutoService {
	@Autowired
	DB db;

	@Autowired
	Authenticated auth;

	@Autowired
	ValidationService validationService;

	public ResponseEntity<Map<String, Object>> index() {
		int perPage = (request("rows") == null | (request("rows")).isBlank()) ? 10 : Integer.parseInt(request("rows"));
		int page = (request("page") == null | (request("page")).isBlank()) ? 1 : Integer.parseInt(request("page"));
		if (perPage > 100) {
			perPage = 100;
		}
		String sql = "select (select code,nameNp,nameEn,disabled,approved from  coactivity.centralActivity order by code ASC offset "
				+ ((page - 1) * perPage) + " rows fetch next " + perPage + " rows only for json auto) as rows";

		Map<String, Object> result = new HashMap<>();
		List<Tuple> rsTuple = db.getResultList(sql);
		System.out.println(rsTuple.get(0).get(0));
		result.put("rows", rsTuple.get(0).get(0));
		result.put("currentPage", page);
		result.put("perPage", perPage);
		sql = "select count(caId) from coactivity.centralActivity ";
		Tuple totalRows = db.getSingleResult(sql);
		result.put("total", totalRows.get(0));
		return Messenger.getMessenger().setData(result).success();
	}

	public ResponseEntity<Map<String, Object>> store() {
		CentralActivity model = new CentralActivity();
		model.loadData(document);

		String sql = "INSERT INTO coactivity.centralActivity(caId, sectorialActivityId, code, nameNp, nameEn, disabled, enterBy, entryDate, approved ) VALUES(dbo.newidint(),?,?,?,?,?,?,GETDATE(),?)";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(model.sectorialActivityId, model.code, model.nameNp,
				model.nameEn, model.disabled, auth.getUserId(), model.approved));
		if (rowEffect.getErrorNumber() == 1) {
			return Messenger.getMessenger().error();

		} else {
			return Messenger.getMessenger().success();
		}

	}

	public ResponseEntity<Map<String, Object>> edit(String id) {
		String sql = "select caId, sectorialActivityId, code, nameNp, nameEn, disabled, enterBy, entryDate, approved from coactivity.centralActivity where caId = ? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}

	}

	public ResponseEntity<Map<String, Object>> update(String id) {
		CentralActivity model = new CentralActivity();
		model.loadData(document);
		String sql = "UPDATE coactivity.centralActivity set SectorialActivityId=?, code=?, nameNp=?, nameEn=?, disabled=?, approved=? where caId=?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(model.sectorialActivityId, model.code, model.nameNp,
				model.nameEn, model.disabled, model.approved, id));
		if (rowEffect.getErrorNumber() == 1) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public ResponseEntity<Map<String, Object>> destroy(String id) {
		if(!isBeingUsed("coactivity.centralActivity", id)) {
		String sql = "DELETE from coactivity.centralActivity where caId=?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(id));
		if (rowEffect.getErrorNumber() == 1) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}
		} else {
			return Messenger.getMessenger().setMessage("Deletion Not Allowed").error();
		}

	}

	public List<Map<String, String>> getCentralActivities() {
		List<Map<String, String>> data = new ArrayList<>();
		String sql = "select caId,code,nameEn,nameNp from coactivity.centralActivity where approved=1 and disabled=0 ";
		if (!document.getElementById("sectorialActivityId").value.isBlank()) {
			sql += " and sectorialActivityId='"
					+ (document.getElementById("sectorialActivityId").value).replace("'", "''") + "'";
		}

		sql += "order by [CODE] Asc";
		List<Tuple> tuples = db.getResultList(sql);
		if (tuples != null) {
			for (Tuple t : tuples) {
				Map<String, String> map = new HashMap<>();
				map.put("caId", t.get("caId") + "");
				map.put("nameEn", t.get("nameEn") + "");
				map.put("nameNp", t.get("nameNp") + "");
				map.put("code", t.get("code") + "");
				data.add(map);
			}

		}
		return data;

	}

}
