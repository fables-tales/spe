package uk.me.graphe.server.database.dbitems;


import uk.me.graphe.shared.Edge;

public class AddEdgeDB extends EdgeDB {

    @Override
    public boolean createsEdge(Edge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public AddEdgeDB(Edge e) {
        super(e);
    }


}
