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
import uk.me.graphe.shared.messages.SetGraphPropertiesMessage;

public class DataManager {

    private static Map<Integer, OTStyleGraphManager2d> sGraphs;
    private static int sHighestId = 0;
    private static Database mDatabase = DatabaseFactory.newInstance();
    private static OTStyleGraphManager2d sInstance = OTGraphManagerFactory.newInstance(1);
    private static Timer mTimer = new Timer();
    
    static {
        newMap();
        create();
        mTimer.scheduleAtFixedRate(new Backup(), 120000, 120000);
        sHighestId = mDatabase.size();
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
        Timer mSaver = new Timer();
        mSaver.schedule(new SaveGraph(graph), 500);
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
    
    static class SaveGraph extends TimerTask {
        
        OTGraphManager2d g = null;
        public SaveGraph(OTGraphManager2d gr) {
            g = gr;
        }
        public void run() {
            mDatabase.store(g);
        }
    }

    public static void renameGraph(int id, String title) {
    	if (sGraphs.containsKey(id))
    		sGraphs.get(id).setName(title);
        else
            mDatabase.rename(id,title);   
    }

    public static void setGraphProperties(int currentGraphId,
            SetGraphPropertiesMessage sgpm) {
        
    }
}