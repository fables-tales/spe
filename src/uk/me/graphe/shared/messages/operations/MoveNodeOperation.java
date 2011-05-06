package uk.me.graphe.shared.messages.operations;


import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

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
        JSONObject repr = JSONImplHolder.make();
        try {
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
        return "movNode";
    }

    @Override
    public void applyTo(GraphManager2d mGraph) {
        mGraph.moveVertexTo(getNode(), mToY, mToX);
    }

}
