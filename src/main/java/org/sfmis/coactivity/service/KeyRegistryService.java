package org.sfmis.coactivity.service;

import java.util.Arrays;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.KeyRegistry;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.DbResponse;
import org.sfmis.coactivity.util.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
@Component
public class KeyRegistryService extends AutoService {
	@Autowired
	DB db;

	@Autowired
	Authenticated auth;

	public ResponseEntity<Map<String, Object>> index() {
		return null;
	}

	public ResponseEntity<Map<String, Object>> store() {
		KeyRegistry model = new KeyRegistry();
		model.loadData(document);
		String sql = "INSERT INTO coactivity.keyRegistry(id, tableName, fieldName, fieldValue, serviceName, serviceTable, entryDate, enterBy) VALUES(dbo.newidint(),?,?,?,?,?,GETDATE(),?)";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(model.tableName, model.fieldName, model.fieldValue,
				model.serviceName, model.serviceTable, auth.getUserId()));
		if (rowEffect.getErrorNumber() == 1) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public ResponseEntity<Map<String, Object>> edit(String id) {
		String sql = "SELECT id, tableName, fieldName, fieldValue, serviceName, serviceTable, entryDate, enterBy from coactivity.keyRegistry where id=? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();

		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}

	}

	public ResponseEntity<Map<String, Object>> update(String id) {
		KeyRegistry model = new KeyRegistry();
		model.loadData(document);
		String sql = "UPDATE coactivity.keyRegistry set tableName=?, fieldName=?, fieldValue=?, serviceName=?, serviceTable=? where id=?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(model.tableName, model.fieldName, model.fieldValue,
				model.serviceName, model.serviceTable, id));
		if (rowEffect.getErrorNumber() == 1) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public ResponseEntity<Map<String, Object>> destroy(String id) {
		if (!isBeingUsed("coactivity.keyRegistry", id)) {
			String sql = "DELETE from coactivity.keyRegistry where id=?";
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

}
