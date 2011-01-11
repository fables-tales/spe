package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.client.Graphemeui;
import uk.me.graphe.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.Vertex;

public class AddNodeOperation extends NodeOperation {

    public AddNodeOperation(Vertex v, int x, int y) {
        super(v);
        mNodeX = x;
        mNodeY = y;
    }

    private int mNodeX;
    private int mNodeY;

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            super.addApplyOn(repr);
            repr.put("message", "addNode");
            repr.put("name", this.getNode().getLabel());
            repr.put("x", mNodeX);
            repr.put("y", mNodeY);
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

    @Override
    public boolean createsNode(Vertex effectedNode) {
        System.out.println(effectedNode);
        System.out.println(this.getNode());
        return effectedNode.getLabel().equals(this.getNode().getLabel());
    }

    public void apply(OTGraphManager2d g) {
        g.addVertex(this.getNode(), mNodeX, mNodeY, Graphemeui.VERTEX_SIZE);
    }

    @Override
    public boolean isNodeOperation() {
        return true;
    }
    
    @Override
    public NodeOperation asNodeOperation() {
        return this;
    }

    public int getX() {
        return mNodeX;
    }
    
    public int getY() {
        return mNodeY;
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }
}
