package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class HeartbeatMessage extends Message {
    
    private static String sRepr;
    
    static {
        try {
            JSONObject result = JSONImplHolder.make();
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
