package uk.me.graphe.server.database.dbitems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;

public class CompositeDB extends GraphDB {

    private List<GraphDB> mOperations;

    public CompositeDB(List<GraphDB> subList) {
        mOperations = new ArrayList<GraphDB>(subList);
    }


    public boolean deletesNode(Vertex effectedNode) {
        for (GraphDB o : mOperations) {
            if (o.isNodeOperation()
                    && o.asNodeOperation().deletesNode(effectedNode)) {
                return true;
            }
        }

        return false;
    }

    public boolean deletesEdge(Edge effectedEdge) {
        for (GraphDB o : mOperations) {
            if (o.isEdgeOperation()
                    && o.asEdgeOperation().deletesEdge(effectedEdge)) {
                return true;
            }
        }

        return false;
    }

    public List<GraphDB> asIndividualOperations() {
        return Collections.unmodifiableList(mOperations);
    }


}
