package org.sfmis.coactivity.config;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.sfmis.coactivity.auth.Authenticated;
import org.sfmis.coactivity.parser.RequestParser;
import org.sfmis.coactivity.util.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	
	private String authServiceUrl = "lb://"+"CENTRAL-SERVICE-MD";

	@Autowired
	Authenticated auth;

	@Autowired
	RequestParser doc;
	
	@Autowired
	private RestTemplate rt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		doc.setRequestParser(request);
		String jwtToken = getTokenfromRequest(request);
		if (jwtToken != null) {
			try {
				/*
				 * @Author: Lifecracker87 Accesses token from request header Validates the token
				 * Set authorized data to a Authrepo, which is accessable from everywhere in the
				 * project using Authenticated servcice
				 */
				System.out.println(jwtToken);
				CommonResponse remData = rt.getForObject(authServiceUrl + "/check-token?_token="+jwtToken, CommonResponse.class);
				if (remData.getStatus() == 1) {
					JSONObject data = remData.getJsonObject();
					String userId = data.getString("userId");
					String orgId = data.getString("orgId");
					String appId = data.getString("appId");
					String adminId= data.getString("adminId");
					
					
					auth.setUserId(userId);
					auth.setOrgId(orgId);
					auth.setAppId(appId);
					auth.setAdminId(adminId);
					UsernamePasswordAuthenticationToken springAuthToken = new UsernamePasswordAuthenticationToken(
							userId, null, new ArrayList<>());
					springAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(springAuthToken);
				} else {
					setHeaderForEx(response, remData.getStatus(), remData.getMessage());
					return;
				}
			} catch (JSONException e) {
				setHeaderForEx(response, 0, "Got Invalid Json Data");
				return;
			}catch(HttpClientErrorException e) {
				try {
					System.out.println(e.getResponseBodyAsString());
					JSONObject res = new JSONObject(e.getResponseBodyAsString());
					setHeaderForEx(response, res.getInt("status"),res.getString("message"));
					return;
				} catch (JSONException e1) {
					setHeaderForEx(response, 0, "Got Invalid Json Data");
					return;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				setHeaderForEx(response, 0,e.getMessage());
				return;
			}
		}
		
		filterChain.doFilter(request, response);
		
	}

	public String getTokenfromRequest(HttpServletRequest request) {
		if (request.getHeader("Authorization") == null) {
			if (doc.getElementById("_token").getValue().isBlank()) {
				return null;
			}
			return doc.getElementById("_token").getValue();
		}
		return request.getHeader("Authorization").replace("Bearer ", "");
	}

	public void setHeaderForEx(HttpServletResponse response, int code, String mesage) throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(message(code, mesage));
	}

	private String message(int code, String mesage) {
		return "{\"status\":" + code + ",\"message\":\"" + mesage + "\"}";
	}

}
