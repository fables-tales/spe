package uk.me.graphe.server.ot;

import uk.me.graphe.graphmanagers.GraphManager2d;

public interface GraphTransform {
    public void apply(GraphOperation op);
    
    public GraphManager2d getState();
    
}
