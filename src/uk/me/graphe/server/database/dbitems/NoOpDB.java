package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;


@Embedded("graph")
public class NoOpDB extends GraphDB {

    
    public NoOpDB() {
    }

    @Override
    public boolean isNoOperation() {
        return true;
    }

}
