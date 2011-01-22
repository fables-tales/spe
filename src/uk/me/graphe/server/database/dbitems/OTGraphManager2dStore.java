package uk.me.graphe.server.database.dbitems;

import java.util.List;

import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.messages.operations.CompositeOperation;

import com.google.code.morphia.annotations.Entity;


@Entity
public class OTGraphManager2dStore {
    private int id;
    private int stateid;
    private List<GraphDB> mOps;
    
    public OTGraphManager2dStore (OTGraphManager2d graph) {
        id = graph.getGraphId();
        stateid = graph.getStateId();
        CompositeOperation history = graph.getCompleteHistory();
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

    public List<GraphDB> getmOps() {
        return mOps;
    }

    public void setmOps(List<GraphDB> mOps) {
        this.mOps = mOps;
    }

}
