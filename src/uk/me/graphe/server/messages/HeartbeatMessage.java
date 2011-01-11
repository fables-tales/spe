package uk.me.graphe.server.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class HeartbeatMessage extends Message {
    
    private static String sRepr;
    
    static {
        try {
            JSONObject result = new JSONObject();
            result.put("message", "heartbeat");
            sRepr = result.toString();
        } catch (JSONException e) {
            throw new Error(e);
        }
    }
    
    @Override
    public String toJson() {
        return sRepr;
    }

    @Override
    public String getMessage() {
        return "heartbeat";
    }

}