package uk.me.graphe.server.database.dbitems;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.VertexDirection;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class DBEdge {

    @Embedded private DBVertex mFrom;
    @Embedded private DBVertex mTo;
    private String mDir;
    
    public DBEdge () {
        
    }
    public DBEdge (Edge edge, DBVertex from, DBVertex to) {
        mFrom = from;
        mTo = to;
        if (edge.getDirection() == VertexDirection.both)
            mDir = "both";
        else if (edge.getDirection() == VertexDirection.fromTo)
            mDir = "fromTo";
        else
            mDir = "toFrom";
    }
    
    public DBVertex getFrom() {
        return mFrom;
    }
    
    public DBVertex getTo() {
        return mTo;
    }
    
    public VertexDirection getDir() {
        if (mDir.equals("both")) {
            return VertexDirection.both;
        }
        else if (mDir.equals("fromTo")) {
            return VertexDirection.fromTo;
        }
        else
            return VertexDirection.toFrom;
    }
}
