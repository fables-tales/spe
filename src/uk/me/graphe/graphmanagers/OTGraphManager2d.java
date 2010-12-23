package uk.me.graphe.graphmanagers;

import java.util.SortedMap;

import uk.me.graphe.server.operations.Operation;

public interface OTGraphManager2d extends GraphManager2d {
    public SortedMap<Long, Operation> getOperationHistory();
    
    public void applyOperation(Operation op);
}
