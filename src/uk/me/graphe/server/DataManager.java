package uk.me.graphe.server;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.me.graphe.server.database.Database;
import uk.me.graphe.server.database.DatabaseFactory;
import uk.me.graphe.server.database.dbitems.OTGraphManager2dStore;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.graphmanagers.OTStyleGraphManager2d;

public class DataManager {

    private static Map<Integer, OTStyleGraphManager2d> sGraphs;
    private static int sHighestId = 0;
    private static Database mDatabase = DatabaseFactory.newInstance();
    private static OTStyleGraphManager2d sInstance = OTGraphManagerFactory.newInstance(1);
    private static Timer mTimer = new Timer();
    
    static {
        newMap();
        mTimer.scheduleAtFixedRate(new Backup(), 1000, 5000);
        sHighestId = mDatabase.size();
        if (sHighestId < 1)
        	create();
    }

    private static void newMap() {
        sGraphs = new LinkedHashMap<Integer, OTStyleGraphManager2d>(20, (float) 0.75, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, OTStyleGraphManager2d> eldest) {
                if (size() > 15) {
                    mDatabase.store(eldest.getValue());
                    return true;
                }
                return false;
            }
        };
    }

    public static OTStyleGraphManager2d getGraph(int mGraphId) {
        if (sGraphs.containsKey(mGraphId))
            return sGraphs.get(mGraphId);
        OTStyleGraphManager2d graph = mDatabase.retrieve(mGraphId);
        if (graph == null)
            return null;
        else
            return graph;
        
    }

    public static void save(OTStyleGraphManager2d graph) {
        if (sGraphs.containsKey(graph.getGraphId())) sGraphs.remove(graph.getGraphId());
        sGraphs.put(graph.getGraphId(), graph);
    }

    public static int create() {

        int id = ++sHighestId;
        OTStyleGraphManager2d  graph = OTGraphManagerFactory.newInstance(id);
        sGraphs.put(id, graph);
        return id;
    }

    public static void flush() {
        newMap();
    }
    
    static class Backup extends TimerTask {
        public void run() {
            Collection<OTStyleGraphManager2d> iterator = sGraphs.values();
            for (OTGraphManager2d g : iterator)
                mDatabase.store(g);
        }
      }
}