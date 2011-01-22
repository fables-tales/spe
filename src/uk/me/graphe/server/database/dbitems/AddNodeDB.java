package uk.me.graphe.server.database.dbitems;


import uk.me.graphe.client.Graphemeui;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;

public class AddNodeDB extends NodeDB {

    public AddNodeDB(Vertex v, int x, int y) {
        super(v);
        mNodeX = x;
        mNodeY = y;
    }

    private int mNodeX;
    private int mNodeY;

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
    public NodeDB asNodeOperation() {
        return this;
    }

    public int getX() {
        return mNodeX;
    }
    
    public int getY() {
        return mNodeY;
    }

}
