package uk.me.graphe.graphmanagers;

public class OTGraphManagerFactory {

    public static OTGraphManager2d newInstance(int id) {
        return new OTGraphManager2dImpl(id);
    }

}
