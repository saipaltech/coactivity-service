package org.sfmis.coactivity.service;
import java.util.Arrays;
import java.util.HashMap;
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
		return null;
	}

	public void create() {

	}

	public Map<String, Object> store() {
		LocalActivity model = new LocalActivity();
		model.loadData(document);
		String sql ="INSERT INTO coactivity.localActivity(laId, activityId, adminId, lmOrgId, code, nameNp, nameEn, disabled, enterBy, enteryDate, approved) VALUES (dbo.newidint(),?,?,?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.activityId, model.adminId, model.lmOrgId, model.code, model.nameNp, model.nameEn, model.disabled, auth.getUserId(), model.approved));
		if(rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}
		
		
		
	}

	public void show(String id) {

	}

	public Map<String, Object> edit(String id) {
		String sql = "SELECT laId, activityId, adminId, lmOrgId, code, nameNp, nameEn, disabled, enterBy, enteryDate, approved from coactivity.localActivity where laId=? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if(result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}
		
	}

	public Map<String, Object> update(String id) {
		LocalActivity model = new LocalActivity();
		model.loadData(document);
		String sql ="UPDATE coactivity.localActivity set activityId =?, adminId=?, lmOrgId =?, code=?, nameNp=?, nameEn=?, disabled=?,approved=? where laId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.activityId, model.adminId, model.lmOrgId, model.code, model.nameNp, model.nameEn, model.disabled, model.approved, id));
		if(rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}
		
	}

	public Map<String, Object> destroy(String id) {
		String sql ="DELETE from coactivity.localActivity where  laId=?";
		int rowEffect = db.executeUpdate(sql,Arrays.asList(id));
		if(rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}
	}
}
