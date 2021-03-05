package org.sfmis.coactivity.model;

import java.lang.reflect.Field;

import org.sfmis.coactivity.parser.RequestParser;

public class KeyRegistry {
	public String id;
	public String tableName;
	public String fieldName;
	public String fieldValue;
	public String serviceName;
	public String serviceTable;
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

}
