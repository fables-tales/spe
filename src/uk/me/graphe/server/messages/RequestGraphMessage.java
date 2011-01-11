package uk.me.graphe.server.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestGraphMessage extends Message {

    private int mGraphId, mStateId;
    
    public RequestGraphMessage(int graph, int myState) {
        mGraphId = graph;
        mStateId = myState;
    }
    
    @Override
    public String getMessage() {
        return "requestGraph";
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            repr.put("message", this.getMessage());
            repr.put("id", mGraphId);
            repr.put("since", mStateId);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

    public int getGraphId() {
        return mGraphId;
    }

    public int getSince() {
        return mStateId;
    }

}
