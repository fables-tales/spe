package uk.me.graphe.server.messages.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;

public class CompositeOperation extends GraphOperation {

    private List<GraphOperation> mOperations;

    public CompositeOperation(List<GraphOperation> subList) {
        mOperations = new ArrayList<GraphOperation>(subList);
    }

    @Override
    public String toJson() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean deletesNode(Vertex effectedNode) {
        for (GraphOperation o : mOperations) {
            if (o.isNodeOperation()
                    && o.asNodeOperation().deletesNode(effectedNode)) {
                return true;
            }
        }

        return false;
    }

    public boolean deletesEdge(Edge effectedEdge) {
        for (GraphOperation o : mOperations) {
            if (o.isEdgeOperation()
                    && o.asEdgeOperation().deletesEdge(effectedEdge)) {
                return true;
            }
        }

        return false;
    }
    
    public List<GraphOperation> asIndividualOperations() {
        return Collections.unmodifiableList(mOperations);
    }

}
