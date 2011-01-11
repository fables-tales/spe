package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.shared.Vertex;

public class DeleteNodeOperation extends NodeOperation {

    public DeleteNodeOperation(Vertex v) {
        super(v);
    }

    @Override
    public boolean deletesNode(Vertex effectedNode) {
        return effectedNode.equals(this.getNode());
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            super.addApplyOn(repr);
            repr.put("message", "delNode");
            repr.put("name", this.getNode().getLabel());
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
