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
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CentralActivityService extends AutoService {
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
		return null;
	}

	public void create() {

	}

	public Map<String, Object> store() {
		CentralActivity model = new CentralActivity();
		model.loadData(document);

		String sql = "INSERT INTO coactivity.centralActivity(caId, sectorialActivityId, code, nameNp, nameEn, disabled, enterBy, entryDate, approved ) VALUES(dbo.newidint(),?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.sectorialActivityId, model.code, model.nameNp,
				model.nameEn, model.disabled, auth.getUserId(), model.approved));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();

		} else {
			return Messenger.getMessenger().success();
		}

	}

	public void show(String id) {

	}

	public Map<String, Object> edit(String id) {
		String sql = "select caId, sectorialActivityId, code, nameNp, nameEn, disabled, enterBy, entryDate, approved from coactivity.centralActivity where caId = ? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}

	}

	public Map<String, Object> update(String id) {
		CentralActivity model = new CentralActivity();
		model.loadData(document);
		String sql = "UPDATE coactivity.centralActivity set SectorialActivityId=?, code=?, nameNp=?, nameEn=?, disabled=?, approved=? where caId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.sectorialActivityId, model.code, model.nameNp,
				model.nameEn, model.disabled, model.approved, id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public Map<String, Object> destroy(String id) {
		String sql = "DELETE from coactivity.centralActivity where caId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}

	}
	
	public List<Map<String, String>> getCentralActivities() {
		List<Map<String, String>> data = new ArrayList<>();
		String sql = "select caId,code,nameEn,nameNp from coactivity.centralActivity where approved=1 and disabled=0 ";
		if (!document.getElementById("sectorialActivityId").value.isBlank()) {
			sql += " and sectorialActivityId='" + (document.getElementById("sectorialActivityId").value).replace("'", "''") + "'";
		}
		
		sql+="order by [CODE] Asc";
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
