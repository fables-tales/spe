package uk.me.graphe.server.database.dbitems;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

@Entity
public class DBGraph {
    
    @Reference private List<DBEdge> mEdges;
    @Reference private List<DBVertex> mVertices;
    
    public DBGraph(List<DBEdge> e, List<DBVertex> v) {
        mEdges = e;
        mVertices = v;
    }
    
    public List<DBEdge> getEdges() {
        return mEdges;
    }
    
    public List<DBVertex> getVertices() {
        return mVertices;
    }
}
