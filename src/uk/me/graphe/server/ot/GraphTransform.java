package uk.me.graphe.server.ot;

import uk.me.graphe.server.messages.operations.CompositeOperation;
import uk.me.graphe.server.messages.operations.EdgeOperation;
import uk.me.graphe.server.messages.operations.GraphOperation;
import uk.me.graphe.server.messages.operations.NoOperation;
import uk.me.graphe.server.messages.operations.NodeOperation;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;

public class GraphTransform {

    public static GraphOperation transform(GraphOperation toApply,
            CompositeOperation historyDelta) {
        
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
