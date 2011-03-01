package uk.me.graphe.server.database;

public class DatabaseFactory {

    public static Database newInstance() {
        return new DatabaseImpl();
    }
}
