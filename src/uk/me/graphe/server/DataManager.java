package uk.me.graphe.server;

import java.util.HashMap;
import java.util.Map;

import uk.me.graphe.server.database.Database;
import uk.me.graphe.server.database.DatabaseFactory;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;

public class DataManager {

    private static Map<Integer, OTGraphManager2d> sGraphs = new HashMap<Integer, OTGraphManager2d>();
    private static int sHighestId = 0;
    private static Database mDatabase= DatabaseFactory.newInstance();
    
    public static OTGraphManager2d getGraph(int mGraphId) {
        if (sGraphs.get(mGraphId) != null)
            return sGraphs.get(mGraphId);
        else 
            return mDatabase.retrieve(mGraphId);
    }

    public static void save(OTGraphManager2d graph) {
        if (sGraphs.containsKey(graph.getGraphId()))
            sGraphs.remove(graph.getGraphId());
        else if (sGraphs.size() >= 16)
                eject();
        sGraphs.put(graph.getGraphId(), graph);
    }
    
    private static void eject() {
        Map.Entry<Integer,OTGraphManager2d> object = sGraphs.entrySet().iterator().next();
        mDatabase.store(object.getValue());
        sGraphs.remove(object.getKey());
    }
    public static int create() {
        int id = ++sHighestId;
        if (sGraphs.size() >= 16)
            eject();
        sGraphs.put(id, OTGraphManagerFactory.newInstance(id));
        return id;
    }
    
    public static void flush() {
        for (OTGraphManager2d item : sGraphs.values())
            mDatabase.store(item);
        sGraphs = new HashMap<Integer, OTGraphManager2d>();
    }
}
