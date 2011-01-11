package uk.me.graphe.server.ot;

import uk.me.graphe.graphmanagers.OTGraphManager2d;
import uk.me.graphe.server.GraphKey;

public interface GraphStateManager {
    
    public GraphKey putGraph(OTGraphManager2d gm);
    
    public OTGraphManager2d getGraph(GraphKey k);
}
