package uk.me.graphe.server;

import java.util.Map;
import java.io.Serializable;

public class UserAuthInfo implements Serializable{
	
	private String redirectionUrl;
	private Map params;
	
	UserAuthInfo(String reUrl, Map p){
		redirectionUrl = reUrl;
		params = p;
	}
	
	UserAuthInfo(){

	}
	
	public String getRedirectionUrl(){
		return redirectionUrl;
	}
	
	public Map getParams(){
		return params;
	}
	
	void setRedirectionUrl(String reUrl){
		redirectionUrl = reUrl;
	}
	
	void setParams(Map p){
		params = p;
	}
	
	
}
