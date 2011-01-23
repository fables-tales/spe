package uk.me.graphe.server.database.dbitems;


import com.google.code.morphia.annotations.Embedded;

import uk.me.graphe.shared.Edge;

@Embedded
public class AddEdgeDB extends EdgeDB {

    @Override
    public boolean createsEdge(Edge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public AddEdgeDB(DBEdge e) {
        super(e);
    }


}
