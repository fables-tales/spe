package uk.me.graphe.server;

import java.util.LinkedHashMap;
import java.util.Map;

import uk.me.graphe.server.database.Database;
import uk.me.graphe.server.database.DatabaseFactory;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;

public class DataManager {

    private static Map<Integer, OTGraphManager2d> sGraphs;
    private static int sHighestId = 0;
    private static Database mDatabase= DatabaseFactory.newInstance();
    
    static {
        newMap();
    }
    
    private static void newMap() {
        sGraphs = new LinkedHashMap<Integer, OTGraphManager2d>(20,(float)0.75,true){
            private static final long serialVersionUID = 1L;
            @Override protected boolean removeEldestEntry (Map.Entry<Integer, OTGraphManager2d> eldest) {
                if (size() > 15) {
                    mDatabase.store(eldest.getValue());
                    return true;
                }
                return false;
            }
        };
        create();
    }
    public static OTGraphManager2d getGraph(int mGraphId) {
        if (sGraphs.get(mGraphId) != null)
            return sGraphs.get(mGraphId);
        else 
            return mDatabase.retrieve(mGraphId);
    }

    public static void save(OTGraphManager2d graph) {
        if (sGraphs.containsKey(graph.getGraphId()))
            sGraphs.remove(graph.getGraphId());
        sGraphs.put(graph.getGraphId(), graph);
    }
    
    public static int create() {
        
        int id = ++sHighestId;
        sGraphs.put(id, OTGraphManagerFactory.newInstance(id));
        return id;
    }
    
    public static void flush() {
        newMap();
    }
}
