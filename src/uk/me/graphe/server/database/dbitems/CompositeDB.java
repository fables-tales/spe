package uk.me.graphe.server.database.dbitems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

@Entity("graph")
public class CompositeDB extends GraphDB {

    @Reference
    private List<GraphDB> mOperations;

    public CompositeDB() {
        
    }
    
    public CompositeDB(List<GraphDB> subList) {
        mOperations = new ArrayList<GraphDB>(subList);
    }


    public boolean deletesNode(DBVertex effectedNode) {
        for (GraphDB o : mOperations) {
            if (o.isNodeOperation()
                    && o.asNodeOperation().deletesNode(effectedNode)) {
                return true;
            }
        }

        return false;
    }

    public boolean deletesEdge(DBEdge effectedEdge) {
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
