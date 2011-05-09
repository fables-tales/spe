package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class AddPrivsMessage extends Message {

    private String emailAddress = null;
    private String graphId = null;
    
    public AddPrivsMessage(String emailAddress, String graphId) {
        this.emailAddress = emailAddress;
        this.graphId = graphId;
    }
    
    @Override
    public String getMessage() {
        return "addPrivs";
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("emailAddress", emailAddress);
            repr.put("graphId", graphId);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

    public String getEmailAddress() {
        return emailAddress;
    } 
    
    public String getGraphId() {
        return graphId;
    }
    
}
