package uk.me.graphe.shared.graphmanagers;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.client.Graphemeui;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.EdgeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;
import uk.me.graphe.shared.messages.operations.MoveNodeOperation;
import uk.me.graphe.shared.messages.operations.NodeOperation;

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
                System.err
                        .println("got weight for edge in ot:" + e.getWeight());
                System.err.println("adding edge with parameters:"
                        + e.getFromVertex() + " " + e.getToVertex() + " " + " "
                        + e.getDirection() + " " + e.getWeight());
                this.addEdge(e.getFromVertex(), e.getToVertex(), e
                        .getDirection(), e.getWeight());
            } else if (eo.deletesEdge(eo.getEdge())) {
                System.err.println(eo.getEdge());
                if (this.getUnderlyingGraph().getEdges().size() > 0) {
                    System.err.println(this.getUnderlyingGraph().getEdges()
                            .get(0));
                    Edge e2 = this.getUnderlyingGraph().getEdges().get(0);
                    System.err.println(e2.equals(eo.getEdge()));
                    System.err.println(e2.getFromVertex() + " "
                            + eo.getEdge().getFromVertex());
                    System.err.println(e2.getToVertex() + " "
                            + eo.getEdge().getToVertex());
                    System.err.println(e2.getDirection() + " "
                            + eo.getEdge().getDirection());
                }
                Edge e = eo.getEdge();
                this.removeEdge(e);
            }
        } else if (graphOperation.isNodeOperation()) {
            NodeOperation no = graphOperation.asNodeOperation();
            if (no.createsNode(no.getNode())) {
                AddNodeOperation ano = (AddNodeOperation) no;
                this.addVertex(ano.getNode(), ano.getX(), ano.getY(),
                        Graphemeui.VERTEX_SIZE);
            } else if (no.deletesNode(no.getNode())) {
                this.removeVertex(no.getNode());
            } else if (no.movesNode(no.getNode())) {
                MoveNodeOperation mno = (MoveNodeOperation) no;
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
        return new CompositeOperation(mHistory.subList(historyId, mHistory
                .size()));
    }

    @Override
    public int getStateId() {
        return mStateId;
    }

    public void setStateId(int m) {
        mStateId = m;
    }

    @Override
    public CompositeOperation getCompleteHistory() {
        return this.getOperationDelta(0);
    }

}
