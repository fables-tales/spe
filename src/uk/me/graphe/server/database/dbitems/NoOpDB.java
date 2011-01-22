package uk.me.graphe.server.database.dbitems;



public class NoOpDB extends GraphDB {

    @Override
    public boolean isNoOperation() {
        return true;
    }

}
