package org.sfmis.coactivity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.model.Sector;
import org.sfmis.coactivity.util.DB;
import org.sfmis.coactivity.util.Messenger;
import org.sfmis.coactivity.util.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SectorService extends AutoService {

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
		String sql = "select (select code,nameNp,nameEn,disabled,approved from  coactivity.sector order by code ASC offset "
				+ ((page - 1) * perPage) + " rows fetch next " + perPage + " rows only for json auto) as rows";

		Map<String, Object> result = new HashMap<>();
		List<Tuple> rsTuple = db.getResultList(sql);
		System.out.println(rsTuple.get(0).get(0));
		result.put("rows", rsTuple.get(0).get(0));
		result.put("currentPage", page);
		result.put("perPage", perPage);
		sql = "select count(sectorId) from coactivity.sector ";
		Tuple totalRows = db.getSingleResult(sql);
		result.put("total", totalRows.get(0));
		return Messenger.getMessenger().setData(result).success();

	}


	public Map<String, Object> store() {
		Sector sector = new Sector();
		sector.loadData(document);

		String sql = "INSERT INTO coactivity.sector(sectorId, broadSectorId, code, nameEn, nameNp, prioritySector, central, province, local, disabled, enterBy, entryDate, approved) VALUES (dbo.newidint(),?,?,?,?,?,?,?,?,?,?,GETDATE(),?)";
		int rowEffect = db.executeUpdate(sql,
				Arrays.asList(sector.broadSectorId, sector.code, sector.nameEn, sector.nameNp, sector.prioritySector,
						sector.central, sector.province, sector.local, sector.disabled, auth.getUserId(),
						sector.approved));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public Map<String, Object> edit(String id) {
		String sql = "select sectorId, broadSectorId, code, nameEn, nameNp, prioritySector, central, province, local, disabled, enterBy, entryDate, approved from coactivity.sector where sectorId = ? for json auto";
		Tuple result = db.getSingleResult(sql, Arrays.asList(id));
		if (result == null) {
			return Messenger.getMessenger().error();

		} else {
			return Messenger.getMessenger().setData(result.get(0)).success();
		}
	}

	public Map<String, Object> update(String id) {
		Sector model = new Sector();
		model.loadData(document);
		String sql = "UPDATE coactivity.sector set broadSectorId=?, code=?, nameEn=?, nameNp=?, prioritySector=?, central=?, province=?, local=?, disabled=?,  approved=? where sectorId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(model.broadSectorId, model.code, model.nameEn, model.nameNp,
				model.prioritySector, model.central, model.province, model.local, model.disabled, model.approved, id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	public Map<String, Object> destroy(String id) {
		String sql = "DELETE from coactivity.sector where sectorId=?";
		int rowEffect = db.executeUpdate(sql, Arrays.asList(id));
		if (rowEffect == 0) {
			return Messenger.getMessenger().setMessage("Invalid Request").error();
		} else {
			return Messenger.getMessenger().success();
		}

	}

	/*
	 * public List<Tuple> getSectors() { String sql =
	 * "select sectorId,code,nameEn from coactivity.sector where approved=1 and disabled=0 order by [CODE] Asc"
	 * ;
	 * 
	 * List<Tuple> tuples = db.getResultList(sql); return tuples; }
	 */

	public List<Map<String, String>> getSectors() {
		List<Map<String, String>> data = new ArrayList<>();
		String sql = "select sectorId,code,nameEn,nameNp from coactivity.sector where approved=1 and disabled=0";
		if (!document.getElementById("broadSectorId").value.isBlank()) {
			sql += " and broadSectorId='" + (document.getElementById("broadSectorId").value).replace("'", "''") + "'";
		}
		sql += " order by [CODE] Asc";
		List<Tuple> tuples = db.getResultList(sql);
		if (tuples != null) {
			for (Tuple t : tuples) {
				Map<String, String> map = new HashMap<>();
				map.put("sectorId", t.get("sectorId") + "");
				map.put("nameEn", t.get("nameEn") + "");
				map.put("nameNp", t.get("nameNp") + "");
				map.put("code", t.get("code") + "");
				data.add(map);
			}

		}
		return data;

	}
}
