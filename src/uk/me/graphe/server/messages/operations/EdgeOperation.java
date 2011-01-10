package uk.me.graphe.server.messages.operations;

import uk.me.graphe.shared.Edge;

public abstract class EdgeOperation extends GraphOperation {

    private Edge mEdge;
    
    public EdgeOperation(Edge e) {
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
