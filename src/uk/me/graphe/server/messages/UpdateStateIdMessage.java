package uk.me.graphe.server.messages;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdateStateIdMessage extends Message {

    private int mStateId;
    
    public UpdateStateIdMessage(int serverStateId) {
        mStateId = serverStateId;
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            repr.put("message", "updateStateId");
            repr.put("newstate", mStateId);
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
