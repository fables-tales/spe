package uk.me.graphe.server.database.dbitems;

import uk.me.graphe.shared.Vertex;

public abstract class NodeDB extends GraphDB {

    private Vertex mNode;
    
    public NodeDB(Vertex v) {
        mNode = v;
    }
    
    @Override
    public boolean isNodeOperation() {
        return true;
    }
    
    @Override
    public NodeDB asNodeOperation() {
        return this;
    }

    public Vertex getNode() {
        return mNode;
    }
    
    public void setNode(Vertex nodeName) {
        mNode = nodeName;
    }

    public boolean deletesNode(Vertex effectedNode) {
        return false;
    }

    public boolean createsNode(Vertex effectedNode) {
        return false;
    }

    public boolean movesNode(Vertex effectedNode) {
        return false;
    }

}
