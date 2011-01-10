package uk.me.graphe.graphmanagers;

import java.util.SortedMap;

import uk.me.graphe.server.messages.MessageFactory;
import uk.me.graphe.server.messages.operations.CompositeOperation;
import uk.me.graphe.server.messages.operations.GraphOperation;

public interface OTGraphManager2d extends GraphManager2d {
    public SortedMap<Long, MessageFactory> getOperationHistory();
    
    public void applyOperation(GraphOperation graphOperation);

    public CompositeOperation getOperationDelta(int historyId);

    public int getStateId();

    public int getGraphId();
}
