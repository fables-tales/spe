package uk.me.graphe.shared;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.EdgeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;
import uk.me.graphe.shared.messages.operations.NoOperation;
import uk.me.graphe.shared.messages.operations.NodeOperation;

public class GraphTransform {

    public static GraphOperation transform(GraphOperation toApply,
            CompositeOperation historyDelta) {
        if (toApply instanceof CompositeOperation) {
            CompositeOperation co = (CompositeOperation)toApply;
            List<GraphOperation> transformed = new ArrayList<GraphOperation>();
            for (GraphOperation o : co.asIndividualOperations()) {
                GraphOperation transformedOp = transform(o, historyDelta);
                transformed.add(transformedOp);
            }
            
            return new CompositeOperation(transformed);
        }        
        
        if (toApply.isNodeOperation()) {
            NodeOperation no = toApply.asNodeOperation();
            Vertex effectedNode = no.getNode();

            if (no.deletesNode(effectedNode) || no.movesNode(effectedNode)) {
                if (historyDelta.deletesNode(effectedNode)) return new NoOperation();
                else return no;
            } else if (no.createsNode(effectedNode)) {
                return no;
            } else if (no.movesNode(effectedNode)) {
                if (historyDelta.deletesNode(effectedNode)) return new NoOperation();
                else return no;
            } else if (no.isStyleOperation() && !historyDelta.deletesNode(effectedNode)) {
                return no;
            } else if (no.isRenameOperation() && !historyDelta.deletesNode(effectedNode)) {
                return no;
            }

        }

        if (toApply.isEdgeOperation()) {
            EdgeOperation eo = toApply.asEdgeOperation();
            Edge effectedEdge = eo.getEdge();
            Vertex fromVertex = effectedEdge.getFromVertex();
            Vertex toVertex = effectedEdge.getToVertex();
            if (historyDelta.deletesNode(fromVertex) || historyDelta.deletesNode(toVertex)) {
                return new NoOperation();
            } else if (eo.deletesEdge(effectedEdge)) {
                if (historyDelta.deletesEdge(effectedEdge)) return new NoOperation();
                else return eo;
            } else if (eo.createsEdge(effectedEdge)) {
                return eo;
            }
        }
        
        throw new Error("transform failed");

    }
}
