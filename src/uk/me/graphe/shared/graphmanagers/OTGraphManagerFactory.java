package uk.me.graphe.shared.graphmanagers;

public class OTGraphManagerFactory {

    public static OTStyleGraphManager2d newInstance(int id) {
        return new OTStyleGraphManager2d(id);
    }

}
