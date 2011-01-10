package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.shared.Vertex;

public class MoveNodeOperation extends NodeOperation {

    @Override
    public boolean movesNode(Vertex effectedNode) {
        return this.getNode().equals(effectedNode);
    }

    private int mToX;
    private int mToY;
    
    public MoveNodeOperation(Vertex v, int toX, int toY) {
        super(v);
        mToX = toX;
        mToY = toY;
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            super.addApplyOn(repr);
            repr.put("message", "movNode");
            repr.put("name", this.getNode().getLabel());
            repr.put("tox", mToX);
            repr.put("toy", mToY);
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

    public int getToX() {
        return mToX;
    }
    
    public int getToY() {
        return mToY;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
