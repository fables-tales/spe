package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;


public class NoOperation extends GraphOperation {

    @Override
    public String toJson() {
        JSONObject jso = new JSONObject();
        try {
            jso.put("message", this.getMessage());
            return jso.toString();
        } catch (JSONException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public boolean isNoOperation() {
        return true;
    }

    @Override
    public String getMessage() {
        return "noOp";
    }

}
