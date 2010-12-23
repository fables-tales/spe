package uk.me.graphe.server.ot;

import uk.me.graphe.graphmanagers.GraphManager2d;
import uk.me.graphe.server.operations.Operation;

public interface GraphTransform {
    public void apply(Operation op);
    
    public GraphManager2d getState();
    
}
