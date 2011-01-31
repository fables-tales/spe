package uk.me.graphe.shared.messages.operations;

import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;


public class NoOperation extends GraphOperation {

    @Override
    public String toJson() {
        JSONObject jso = JSONImplHolder.make();;
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

    @Override
    public void applyTo(GraphManager2d mGraph) {
        
    }

}
