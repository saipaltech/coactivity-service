package org.sfmis.coactivity.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.sfmis.coactivity.parser.RequestParser;

public class SectorialActivity {
	public String saId;
	public String sectorId;
	public String code;
	public String nameNp;
	public String nameEn;
	public String disabled;
	public String approved;

	public void loadData(RequestParser doc) {
		for (Field f : this.getClass().getFields()) {
			String fname = f.getName();
			try {
				f.set(this, doc.getElementById(fname).value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
	}

	public static Map<String, String> rules() {
		Map<String, String> rules = new HashMap<>();
		rules.put("code", "required");
		return rules;
	}
}
