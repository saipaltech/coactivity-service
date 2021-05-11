package org.sfmis.coactivity.auth;


import org.sfmis.coactivity.ApplicationContextProvider;
import org.sfmis.coactivity.util.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Authenticated {
	@Autowired
	DB db;

	public String getToken() {
		return getAuthRequest().token;
	}

	public void setToken(String token) {
		getAuthRequest().token = token;
	}
	
	public Authrepo getAuthRequest() {
		return ApplicationContextProvider.getBean(Authrepo.class);
	}

	public String getUserId() {
		return getAuthRequest().userId;
	}

	public void setUserId(String userId) {
		getAuthRequest().userId = userId;
	}

	public String getOrgId() {
		return getAuthRequest().orgId;
	}

	public void setOrgId(String userId) {
		getAuthRequest().orgId = userId;
	}

	
	
	
	public String getAdminId() {
		return getAuthRequest().adminId;
	}

	public void setAdminId(String adminId) {
		getAuthRequest().adminId = adminId;
	}
	


	
	public void setAppId(String appId) {
		getAuthRequest().appId = appId;
	}

	public String getAppId() {
		return getAuthRequest().appId;
	}

	public void setExtraInfo(String key, Object value) {
		getAuthRequest().extraInfo.put(key, value);
	}

	public Object getExtraInfo(String key) {
		return getAuthRequest().extraInfo.get(key);
	}
	public void setLang(String value) {
		getAuthRequest().extraInfo.put("lang", value);
	}

	public String getLang() {
		Object lang = getAuthRequest().extraInfo.get("lang");
		return (lang==null)?"Np":lang+"";
	}
}

