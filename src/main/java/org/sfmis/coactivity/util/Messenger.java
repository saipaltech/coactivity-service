package org.sfmis.coactivity.util;



import java.util.HashMap;
import java.util.Map;

public class Messenger {

	private Map<String, Object> info = new HashMap<>();
	
	public static Messenger getMessenger() {
		return (new Messenger());
	}
	
	public Messenger setMessage(String message) {
		info.put("message", message);
		return this;
	}
	
	public Messenger setData(Object data) {
		info.put("data", data);
		return this;
	}
	
	public Messenger setStatus(int status) {
		info.put("status", status);
		return this;
	}
	

	public Map<String, Object> success() {
		setStatus(1);
		if(info.get("message") == null) {
			setMessage("Operation Successful.");
		}
		return info;
	}
	
	public Map<String, Object> error() {
		setStatus(0);
		if(info.get("message") == null) {
			setMessage("Operation Failed.");
		}
		return info;
	}
	
	public Map<String, Object> info() {
		setStatus(3);
		if(info.get("message") == null) {
			setMessage("Operation Successful.");
		}
		return info;
	}
	
	public Map<String, Object> warning() {
		setStatus(2);
		if(info.get("message") == null) {
			setMessage("Operation succeeded with warning.");
		}
		return info;
	}
	
	public Map<String, Object> getMessage() {
		return info;
	}
}
