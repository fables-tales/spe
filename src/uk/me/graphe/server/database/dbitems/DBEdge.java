package uk.me.graphe.server.database.dbitems;

import java.util.Map;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

@Entity
public class DBEdge {

    @Reference DBVertex from;
    @Reference DBVertex to;
    String dir;
    
    public DBEdge (Edge edge, Map<Vertex,DBVertex> map) {
        from = map.get(edge.getFromVertex());
        to = map.get(edge.getToVertex());
        if (edge.getDirection() == VertexDirection.both)
            dir = "both";
        else if (edge.getDirection() == VertexDirection.fromTo)
            dir = "fromTo";
        else
            dir = "toFrom";
    }
    
    public DBVertex getFrom() {
        return from;
    }
    
    public DBVertex getTo() {
        return to;
    }
    
    public VertexDirection getDir() {
        if (dir.equals("both")) {
            return VertexDirection.both;
        }
        else if (dir.equals("fromTo")) {
            return VertexDirection.fromTo;
        }
        else
            return VertexDirection.toFrom;
    }
}
