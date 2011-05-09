package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class UserAuthMessage extends Message {

	private String opUrl = null;
	private String reUrl = null;
	private String emailAdd = null;
	private String openIdUrl = null;
	private String authKey = null;
	private String identity = null;
	private String authd = null;
		
	public UserAuthMessage(String opUrl){
		this.opUrl = opUrl;
	}
	
	public UserAuthMessage(String opUrl, String reUrl){
		this.opUrl = opUrl;
		this.reUrl = reUrl;
	}
	
	public UserAuthMessage(String opUrl, String reUrl, String emailAdd){
		this.opUrl = opUrl;
		this.reUrl = reUrl;
		this.emailAdd = emailAdd;
	}
	
	public UserAuthMessage(String opUrl, String reUrl, String emailAdd, String openIdUrl){
		this.opUrl = opUrl;
		this.reUrl = reUrl;
		this.emailAdd = emailAdd;
		this.openIdUrl = openIdUrl;
	}	
	
	public UserAuthMessage(String opUrl, String reUrl, String emailAdd, String openIdUrl, String authKey){
		this.opUrl = opUrl;
		this.reUrl = reUrl;
		this.emailAdd = emailAdd;
		this.openIdUrl = openIdUrl;
		this.authKey = authKey;
	}
	
    public UserAuthMessage(String opUrl, String reUrl, String emailAdd, String openIdUrl, String authKey, String identity){
        this.opUrl = opUrl;
        this.reUrl = reUrl;
        this.emailAdd = emailAdd;
        this.openIdUrl = openIdUrl;
        this.authKey = authKey;
        this.identity = identity;
    }   
    
    public UserAuthMessage(String opUrl, String reUrl, String emailAdd, String openIdUrl, String authKey, String identity, String authd){
        this.opUrl = opUrl;
        this.reUrl = reUrl;
        this.emailAdd = emailAdd;
        this.openIdUrl = openIdUrl;
        this.authKey = authKey;
        this.identity = identity;
        this.authd = authd;
    }   
	
	public UserAuthMessage(){
		
	}
	
	@Override
	public String getMessage() {
		return "userAuth";
	}

	@Override
	public String toJson() {
		JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("opUrl", opUrl);
            repr.put("reUrl", reUrl);
            repr.put("emailAdd", emailAdd);
            repr.put("openIdUrl", openIdUrl);
            repr.put("authKey", authKey);
            repr.put("identity", identity);
            repr.put("authd", authd);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
	}

	public String getOpUrl() {
		return opUrl;
	}
	
	public String getRedirectionUrl() {
		return reUrl;
	}
	
	public String getEmailAddress() {
		return emailAdd;
	}
	
	public void setOpUrl(String inp) {
		opUrl = inp;
	}
	
	public void setRedirectionUrl(String inp){
		reUrl = inp;
	}
	
	public void setEmailAddress(String inp){
		emailAdd = inp;
	}
	
	public void setOpenIdUrl(String inp){
		openIdUrl = inp;
	}
	
	public String getOpenIdUrl() {
		return openIdUrl;
	}
	
	public String getAuthKey() {
		return authKey;
	}
	
	public void setAuthKey(String inp) {
		authKey = inp;
	}
	
	public String getId(){
	    return identity;
	}
	
	public void setId(String inp){
	    identity = inp;
	}
	
	public void setAuthd(boolean inp){
	    if(inp)
	        authd = "true";
	    else
	        authd = "false";
	}
	
	public boolean isAuthd(){
	    if(authd == "true")
	        return true;
	    else
	        return false;
	}
	
}
