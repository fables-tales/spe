package uk.me.graphe.server.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class StateIdMessage extends Message {

    private int mGraph, mState;
    
    public StateIdMessage(int graph, int state) {
        mState = state;
        mGraph = graph;
    }
    
    @Override
    public String toJson() {
        JSONObject jso = new JSONObject();
        try {
            jso.put("message", this.getMessage());
            jso.put("graph", mGraph);
            jso.put("state", mState);
            return jso.toString();
        } catch (JSONException jse) {
            throw new Error(jse);
        }
    }

    @Override
    public String getMessage() {
        return "updateStateId";
    }

    public int getGraphId() {
        return mGraph;
    }
    
    public int getState() {
        return mState;
    }

}
