package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;

@Embedded("graph")
public class AddNodeDB extends NodeDB {

    public AddNodeDB() {
        
    }
    
    public AddNodeDB(DBVertex v, int x, int y) {
        super(v);
        mNodeX = x;
        mNodeY = y;
    }

    private int mNodeX;
    private int mNodeY;

    @Override
    public boolean createsNode(DBVertex effectedNode) {
        System.out.println(effectedNode);
        System.out.println(this.getNode());
        return effectedNode.getLabel().equals(this.getNode().getLabel());
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
