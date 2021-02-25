package org.sfmis.coactivity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.Activity;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivityService extends AutoService {
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
		Activity model = new Activity();
		model.loadData(document);

		String sql = "INSERT INTO  coactivity.activity(activityId, centralActivityId, code, nameEn, nameNp, descEn, descNp, cofog, disabled, enterBy, entryDate, approved) VALUES(dbo.newidint(),?,?,?,?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql,
				Arrays.asList(model.centralActivityId, model.code, model.nameEn, model.nameNp, model.descEn,
						model.descNp, model.cofog, model.disabled, auth.getUserId(), model.approved));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public void show(String id) {

	}

	public Map<String, Object> edit(String id) {
		String sql ="SELECT activityId, centralActivityId, code, nameEn, nameNp, descEn, descNp, cofog, disabled, enterBy, entryDate,approved from coactivity.activity where activityId=? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if(result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}
	}

	public Map<String, Object> update(String id) {
		Activity model = new Activity();
		model.loadData(document);
		String sql ="UPDATE coactivity.activity set centralActivityId=?, code=?, nameEn=?, nameNp=?, descEn=?, descNp=?, cofog=?, disabled=?, approved=? where activityId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.centralActivityId, model.code, model.nameEn, model.nameNp, model.descEn, model.descNp, model.cofog, model.disabled, model.approved,id));
		if(rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}
	}

	public Map<String, Object> destroy(String id) {
		String sql ="DELETE from coactivity.activity where activityId =?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}
		
	}

	public List<Map<String, String>> getActivities() {
		List<Map<String, String>> data = new ArrayList<>();
		String sql = "select activityId,code,nameEn,nameNp from coactivity.activity where approved=1 and disabled=0 ";
		if (!document.getElementById("centralActivityId").value.isBlank()) {
			sql += " and centralActivityId='" + (document.getElementById("centralActivityId").value).replace("'", "''") + "'";
		}
		
		sql+="order by [CODE] Asc";
		List<Tuple> tuples = db.getResultList(sql);
		if (tuples != null) {
			for (Tuple t : tuples) {
				Map<String, String> map = new HashMap<>();
				map.put("activityId", t.get("activityId") + "");
				map.put("nameEn", t.get("nameEn") + "");
				map.put("nameNp", t.get("nameNp") + "");
				map.put("code", t.get("code") + "");
				data.add(map);
			}

		
	}
		return data;
	
}
	}

