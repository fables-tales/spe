package uk.me.graphe.server;

import java.util.HashMap;
import java.util.Map;

import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;

public class DataManager {

    private static Map<Integer, OTGraphManager2d> sGraphs = new HashMap<Integer, OTGraphManager2d>();
    private static int sHighestId = 0;
    
    public static OTGraphManager2d getGraph(int mGraphId) {
        return sGraphs.get(mGraphId);
    }

    public static void save(OTGraphManager2d graph) {
        sGraphs.remove(graph.getGraphId());
        sGraphs.put(graph.getGraphId(), graph);
        
    }
    
    public static int create() {
        int id = ++sHighestId;
        sGraphs.put(id, OTGraphManagerFactory.newInstance(id));
        return id;
    }
    
    public static void flush() {
        sGraphs = new HashMap<Integer, OTGraphManager2d>();
    }

}
