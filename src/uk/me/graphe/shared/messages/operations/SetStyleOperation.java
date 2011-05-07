package uk.me.graphe.shared.messages.operations;

import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class SetStyleOperation extends NodeOperation {

    
    private int mStyle;
    
    public SetStyleOperation(Vertex node, int styleCode) {
        super(node);
        mStyle = styleCode;
    }
    
    @Override
    public void applyTo(GraphManager2d mGraph) {
        mGraph.setVertexStyle(this.getNode(), this.mStyle);
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", getMessage());
            repr.put("name", this.getNode().getLabel());
            repr.put("style", this.mStyle);
            
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }
     
    @Override
    public String getMessage() {
        return "setStyle";
    }
    
    @Override
    public boolean isStyleOperation() {
        return true;
    }

}
