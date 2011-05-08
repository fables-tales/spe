package uk.me.graphe.shared.messages.operations;

import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class RenameNodeOperation extends NodeOperation {
    
    private String mNewName;
    
    public RenameNodeOperation(Vertex v, String newN) {
        super(v);
        mNewName = newN;
        
    }
    
    
    @Override
    public void applyTo(GraphManager2d mGraph) {
        mGraph.renameVertex(getNode().getLabel(), mNewName);
    }

    @Override
    public String getMessage() {
        return "rename";
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", getMessage());
            repr.put("oldNode", getNode().getLabel());
            repr.put("newNode", mNewName);
        } catch (JSONException e) {
            throw new Error(e);
        }
        
        return repr.toString();
    }
    
    @Override
    public boolean isRenameOperation() {
        return true;
    }

}
