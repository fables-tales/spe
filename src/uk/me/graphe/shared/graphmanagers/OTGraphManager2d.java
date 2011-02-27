package uk.me.graphe.shared.graphmanagers;

import java.util.List;

import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;

public interface OTGraphManager2d extends GraphManager2d {

    /**
     * applies an operation to the graph
     * @param graphOperation
     */
    public void applyOperation(GraphOperation graphOperation);

    /**
     * returns an operation that represents the diff between now and the passed
     * history id
     * 
     * @param historyId
     * @return
     */
    public CompositeOperation getOperationDelta(int historyId);

    public int getStateId();

    public int getGraphId();
    
    public void setStateId(int m);
    
    public CompositeOperation getCompleteHistory();

    public void setHistory(List<GraphOperation> h);
}
