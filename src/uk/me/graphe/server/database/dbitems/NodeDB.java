package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

@Embedded("graph")
public abstract class NodeDB extends GraphDB {

    @Embedded
    private DBVertex mNode;
    
    public NodeDB() {
        
    }
    public NodeDB(DBVertex v) {
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

    public DBVertex getNode() {
        return mNode;
    }
    
    public void setNode(DBVertex nodeName) {
        mNode = nodeName;
    }

    public boolean deletesNode(DBVertex effectedNode) {
        return false;
    }

    public boolean createsNode(DBVertex effectedNode) {
        return false;
    }

    public boolean movesNode(DBVertex effectedNode) {
        return false;
    }

}
