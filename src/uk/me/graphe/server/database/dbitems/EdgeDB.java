package uk.me.graphe.server.database.dbitems;

import uk.me.graphe.shared.Edge;

public abstract class EdgeDB extends GraphDB {

    @Override
    public EdgeDB asEdgeOperation() {
        return this;
    }

    @Override
    public boolean isEdgeOperation() {
        return true;
    }

    private Edge mEdge;
    
    public EdgeDB(Edge e) {
        mEdge = e;
    }
    
    public Edge getEdge() {
        return mEdge;
    }

    public boolean deletesEdge(Edge effectedEdge) {
        return false;
    }

    public boolean createsEdge(Edge effectedEdge) {
        return false;
    }

}
