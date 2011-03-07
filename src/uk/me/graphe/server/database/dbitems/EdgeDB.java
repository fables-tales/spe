package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;

@Embedded("graph")
public abstract class EdgeDB extends GraphDB {

    public EdgeDB() {
        
    }
    @Override
    public EdgeDB asEdgeOperation() {
        return this;
    }

    @Override
    public boolean isEdgeOperation() {
        return true;
    }

    @Embedded
    private DBEdge mEdge;
    
    public EdgeDB(DBEdge e) {
        mEdge = e;
    }
    
    public DBEdge getEdge() {
        return mEdge;
    }

    public boolean deletesEdge(DBEdge effectedEdge) {
        return false;
    }

    public boolean createsEdge(DBEdge effectedEdge) {
        return false;
    }

}
