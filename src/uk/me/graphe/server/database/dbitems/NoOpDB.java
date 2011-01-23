package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;


@Embedded
public class NoOpDB extends GraphDB {

    @Override
    public boolean isNoOperation() {
        return true;
    }

}
