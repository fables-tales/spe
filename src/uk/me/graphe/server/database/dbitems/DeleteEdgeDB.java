package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;

@Embedded("graph")
public class DeleteEdgeDB extends EdgeDB {

    public DeleteEdgeDB() {
        
    }
    @Override
    public boolean deletesEdge(DBEdge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public DeleteEdgeDB(DBEdge storeEdge) {
        super(storeEdge);
    }

}
