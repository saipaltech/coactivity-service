package org.sfmis.coactivity.util;

import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class CommonResponse {
	public String message;
	public int status;
	public Object data;

	public String  getData() {
		if(data instanceof List) {
			JSONArray dt = new JSONArray((List) data);
			return dt.toString();
		}else if (data instanceof Map) {
			JSONObject dt = new JSONObject((Map) data);
			return dt.toString();
		}
		return (String) data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() { 
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public JSONArray getJsonArray() throws JSONException {
		try {
			if(data instanceof List) {
				JSONArray dt = new JSONArray((List) data);
				return dt;
			}else if (data instanceof String) {
				if((data+"").startsWith("[")) {
					JSONArray dt = new JSONArray(data+"");
					return dt;
				}
			}
		}catch (JSONException e) {
			return null;
		}
		return null;
	}
	
	public JSONObject getJsonObject() throws JSONException {
		try {
			if(data instanceof Map) {
				JSONObject dt = new JSONObject((Map) data);
				return dt;
			}else if (data instanceof String) {
				if((data+"").startsWith("{")) {
					JSONObject dt = new JSONObject(data+"");
					return dt;
				}
			}
		}catch (JSONException e) {
			return null;
		}
		return null;
	}
	
	public String getStringifiedJsonString(String jdata) {
		return (jdata.replaceAll(":([0-9]{15}\\.?[0-9]+)", ":\"$0\"")).replace(":\":",":\"");
	}
}