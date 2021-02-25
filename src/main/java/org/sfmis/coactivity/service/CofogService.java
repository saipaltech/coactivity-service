package org.sfmis.coactivity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.Cofog;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CofogService extends AutoService {

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
		Cofog model = new Cofog();
		model.loadData(document);

		String sql = "INSERT INTO coactivity.cofog(cofogId, parentId, code, nameEn,nameNp, descriptionNp, descriptionEn, orders, levels, keys, keyNumber, disabled, enterBy, entryDate, approved) VALUES (dbo.newidint(),?,?,?,?,?,?,?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql,
				Arrays.asList(model.parentId, model.code, model.nameEn, model.nameNp, model.descriptionNp,
						model.descriptionEn, model.orders, model.levels, model.keys, model.keyNumber, model.disabled,
						auth.getUserId(), model.approved));

		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}
	}

	public void show(String id) {

	}

	public Map<String, Object> edit(String id) {
		String sql = "select cofogId, parentId, code, nameEn, nameNp, descriptionNp, descriptionEn, orders, levels, keys,keyNumber, disabled, approved from coactivity.cofog where cofogId = ? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}

	}

	public Map<String, Object> update(String id) {
		Cofog model = new Cofog();
		model.loadData(document);
		String sql = "UPDATE coactivity.cofog set parentId=?, code=?, nameEn=?, nameNp=?, descriptionNp=?, descriptionEn=?, orders=?, levels=?, keys=?,keyNumber=?, disabled=?, approved=? where cofogId=?";
		int rowEffect = db.executeUpdate(sql,
				Arrays.asList(model.parentId, model.code, model.nameEn, model.nameNp, model.descriptionNp,
						model.descriptionEn, model.orders, model.levels, model.keys, model.keyNumber, model.disabled,
						model.approved,id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}
	}

	public Map<String, Object> destroy(String id) {
		String sql="DELETE from coactivity.cofog where cofogId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(id));
		if(rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Requst").error();
		} else {
			return Messenger.getMessenger().success();
		}
		
	}
	public List<Map<String, String>> getCofog() {
		List<Map<String, String>> data = new ArrayList<>();
		String sql = "select cofogId,code,nameEn,nameNp from coactivity.cofog where approved=1 and disabled=0 ";
		if (!document.getElementById("parentId").value.isBlank()) {
			sql += " and parentId='" + (document.getElementById("parentId").value).replace("'", "''") + "'";
		}
		
		sql+="order by [CODE] Asc";
		List<Tuple> tuples = db.getResultList(sql);
		if (tuples != null) {
			for (Tuple t : tuples) {
				Map<String, String> map = new HashMap<>();
				map.put("cofogId", t.get("cofogId") + "");
				map.put("nameEn", t.get("nameEn") + "");
				map.put("nameNp", t.get("nameNp") + "");
				map.put("code", t.get("code") + "");
				data.add(map);
			}

		
	}
		return data;
	
}
}
