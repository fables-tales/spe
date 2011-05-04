package uk.me.graphe.server.database.dbitems;

import java.util.List;

import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;


@Entity
public class OTGraphManager2dStore {
    private int id;
    private int stateid;
    private List<String> mOps;
    
    public OTGraphManager2dStore (OTGraphManager2d graph) {
        id = graph.getGraphId();
        stateid = graph.getStateId();
    }
    
    public OTGraphManager2dStore() {
        
    }

    public int getStateid() {
        return stateid;
    }

    public void setStateid(int stateid) {
        this.stateid = stateid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getmOps() {
        return mOps;
    }

    public void setmOps(List<String> mOps) {
        this.mOps = mOps;
    }

}
