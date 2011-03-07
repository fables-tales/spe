package uk.me.graphe.server.database.dbitems;


import com.google.code.morphia.annotations.Embedded;


@Embedded("graph")
public class AddEdgeDB extends EdgeDB {

    public AddEdgeDB() {
        
    }
    @Override
    public boolean createsEdge(DBEdge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public AddEdgeDB(DBEdge e) {
        super(e);
    }


}
