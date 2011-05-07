package uk.me.graphe.shared.messages.operations;

import uk.me.graphe.shared.Vertex;

public abstract class NodeOperation extends GraphOperation {

    private Vertex mNode;
    
    public NodeOperation(Vertex v) {
        mNode = v;
    }
    
    @Override
    public boolean isNodeOperation() {
        return true;
    }
    
    @Override
    public NodeOperation asNodeOperation() {
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

    public boolean isStyleOperation() {
        return false;
    }

}
