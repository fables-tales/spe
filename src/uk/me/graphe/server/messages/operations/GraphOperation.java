package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;

public abstract class GraphOperation extends Message {    

    /**
     * the history id of the graph to apply the operation against
     */
    private int mApplyOn;
    
    protected void addApplyOn(JSONObject jso) throws JSONException {
        jso.put("apply.on", mApplyOn);
    }
    
    public void setHistoryId(int i) {
        mApplyOn = i;
    }
    
    public int getHistoryId() {
        return mApplyOn;
    }

    public boolean isEdgeOperation() {
        return false;
    }
    
    public boolean isNodeOperation() {
        return false;
    }

    public NodeOperation asNodeOperation() {
        return null;
    }

    public EdgeOperation asEdgeOperation() {
        return null;
    }
    
    public void setApplyOn(int id) {
        mApplyOn = id;
    }

    public boolean isNoOperation() {
        return false;
    }
    
    @Override
    public boolean isOperation() {
        return true;
    }

}
