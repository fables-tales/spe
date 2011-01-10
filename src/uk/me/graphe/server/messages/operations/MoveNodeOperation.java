package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.shared.Vertex;

public class MoveNodeOperation extends NodeOperation {

    @Override
    public boolean movesNode(Vertex effectedNode) {
        return this.getNode().equals(effectedNode);
    }

    private int mDx;
    private int mDy;
    
    public MoveNodeOperation(Vertex v, int dx, int dy) {
        super(v);
        mDx = dx;
        mDy = dy;
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            super.addApplyOn(repr);
            repr.put("message", "movNode");
            repr.put("name", this.getNode().getLabel());
            repr.put("dx", mDx);
            repr.put("dy", mDy);
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

}
