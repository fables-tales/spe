package uk.me.graphe.server.database.dbitems;


import uk.me.graphe.shared.Edge;

public class DeleteEdgeDB extends EdgeDB {

    @Override
    public boolean deletesEdge(Edge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public DeleteEdgeDB(Edge e) {
        super(e);
    }

}
