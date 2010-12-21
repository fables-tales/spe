package uk.me.graphe.graphmanagers;

import java.util.SortedMap;

import uk.me.graphe.server.ot.GraphOperation;

public interface OTGraphManager2d extends GraphManager2d {
    public SortedMap<Long, GraphOperation> getOperationHistory();
    
    public void applyOperation(GraphOperation op);
}
