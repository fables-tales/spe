package uk.me.graphe.graphmanagers;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.client.Graphemeui;
import uk.me.graphe.server.messages.operations.AddNodeOperation;
import uk.me.graphe.server.messages.operations.CompositeOperation;
import uk.me.graphe.server.messages.operations.EdgeOperation;
import uk.me.graphe.server.messages.operations.GraphOperation;
import uk.me.graphe.server.messages.operations.MoveNodeOperation;
import uk.me.graphe.server.messages.operations.NodeOperation;
import uk.me.graphe.shared.Edge;

public class OTGraphManager2dImpl extends GraphManager2dImpl implements
        OTGraphManager2d {
    
    private int mId;
    private int mStateId;
    private List<GraphOperation> mHistory = new ArrayList<GraphOperation>();
    
    public OTGraphManager2dImpl(int id) {
        super();
        mId = id;
        mStateId = 0;
    }

    @Override
    public void applyOperation(GraphOperation graphOperation) {
        if (graphOperation.isNoOperation()) {
            this.mStateId++;
            return;
            
        } else if (graphOperation.isEdgeOperation()) {
            EdgeOperation eo = graphOperation.asEdgeOperation();
            if (eo.createsEdge(eo.getEdge())) {
                Edge e = eo.getEdge();
                this.addEdge(e.getFromVertex(), e.getToVertex(), e.getDirection());
            } else if (eo.deletesEdge(eo.getEdge())) {
                Edge e = eo.getEdge();
                this.removeEdge(e);
            }
        } else if (graphOperation.isNodeOperation()) {
            NodeOperation no = graphOperation.asNodeOperation();
            if (no.createsNode(no.getNode())) {
                AddNodeOperation ano = (AddNodeOperation)no;
                this.addVertex(ano.getNode(), ano.getX(), ano.getY(), Graphemeui.VERTEX_SIZE);
            } else if (no.deletesNode(no.getNode())) {
                this.removeVertex(no.getNode());
            } else if (no.movesNode(no.getNode())) {
                MoveNodeOperation mno = (MoveNodeOperation)no;
                this.moveVertexTo(mno.getNode(), mno.getToX(), mno.getToY());
            }
            
        } else {
            throw new Error("weird operation stuff");
        }
        
        mHistory.add(graphOperation);
        this.mStateId++;
        
    }

    @Override
    public int getGraphId() {
        return mId;
    }

    @Override
    public CompositeOperation getOperationDelta(int historyId) {
        return new CompositeOperation(mHistory.subList(historyId, mHistory.size()));
    }

    @Override
    public int getStateId() {
        return mStateId;
    }

}
