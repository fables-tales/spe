package uk.me.graphe.shared;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.messages.operations.AddEdgeOperation;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.DeleteEdgeOperation;
import uk.me.graphe.shared.messages.operations.DeleteNodeOperation;
import uk.me.graphe.shared.messages.operations.MoveNodeOperation;
import uk.me.graphe.shared.messages.operations.NoOperation;

public class OTGraphManagerTest extends TestCase {

    private OTGraphManager2d mManager;

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        mManager = OTGraphManagerFactory.newInstance(17);
    }

    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
        mManager = null;
    }

    public void testStateId_nops() {
        for (int i = 0; i < 1024; i++) {
            Assert.assertEquals(i, mManager.getStateId());
            mManager.applyOperation(new NoOperation());
        }

    }

    public void testStateId_addNodeOps() {
        for (int i = 0; i < 1024; i++) {
            Assert.assertEquals(i, mManager.getStateId());
            mManager.applyOperation(new AddNodeOperation(new Vertex("faces"),
                    0, 0));
        }
    }

    public void testStateId_delNodeOps() {
        for (int i = 0; i < 1024; i++) {
            Assert.assertEquals(i, mManager.getStateId());
            mManager
                    .applyOperation(new DeleteNodeOperation(new Vertex("bees")));
        }
    }

    public void testStateId_moveNodeOps() {
        mManager.addVertex(new Vertex("cows"), 0, 0, 10);
        for (int i = 0; i < 1024; i++) {
            Assert.assertEquals(i, mManager.getStateId());
            mManager.applyOperation(new MoveNodeOperation(new Vertex("cows"),
                    100, 100));
        }
    }

    public void testStateId_addEdgeOps() {
        Vertex v1 = new Vertex("cows");
        Vertex v2 = new Vertex("cows");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);
        for (int i = 0; i < 1024; i++) {
            Assert.assertEquals(i, mManager.getStateId());
            mManager.applyOperation(new AddEdgeOperation(new Edge(v1, v2)));
        }
    }

    public void testStateId_remEdgeOps() {
        Vertex v1 = new Vertex("cows");
        Vertex v2 = new Vertex("cows");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);

        for (int i = 0; i < 1024; i++) {
            mManager.addEdge(v1, v2, VertexDirection.both, 0);
            Assert.assertEquals(i, mManager.getStateId());
            mManager.applyOperation(new DeleteEdgeOperation(new Edge(v1, v2)));
        }
    }

    public void testMoveOp() {
        mManager.addVertex(new Vertex("cows"), 0, 0, 10);
        MoveNodeOperation mov = new MoveNodeOperation(new Vertex("cows"), 17,
                32);
        mManager.applyOperation(mov);
        VertexDrawable vd = mManager.getVertexDrawables().iterator().next();
        Assert.assertEquals("cows", vd.getLabel());
        Assert.assertEquals(17, vd.getCenterX());
        Assert.assertEquals(32, vd.getCenterY());
    }

    public void testAddNodeOp() {
        mManager
                .applyOperation(new AddNodeOperation(new Vertex("cows"), -2, 14));
        VertexDrawable vd = mManager.getVertexDrawables().iterator().next();
        Assert.assertEquals("cows", vd.getLabel());
        Assert.assertEquals(-2, vd.getCenterX());
        Assert.assertEquals(14, vd.getCenterY());
    }

    public void testDelNodeOp_empty() {
        mManager.addVertex(new Vertex("cows"), 0, 0, 10);
        mManager.applyOperation(new DeleteNodeOperation(new Vertex("cows")));
        Assert.assertEquals(0, mManager.getVertexDrawables().size());
    }

    public void testDelNodeOp_full() {
        mManager.addVertex(new Vertex("cows"), 0, 0, 10);
        mManager.addVertex(new Vertex("cows2"), 0, 0, 10);
        mManager.applyOperation(new DeleteNodeOperation(new Vertex("cows")));
        Assert.assertEquals(1, mManager.getVertexDrawables().size());
        VertexDrawable vd = mManager.getVertexDrawables().iterator().next();
        Assert.assertEquals("cows2", vd.getLabel());
    }
    
    public void testAddEdge() {
        Vertex v1 = new Vertex("cows");
        Vertex v2 = new Vertex("cows");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);
        mManager.applyOperation(new AddEdgeOperation(new Edge(v1, v2)));
        List<Edge> e = mManager.getUnderlyingGraph().getEdges();
        Assert.assertEquals(1, e.size());
        Assert.assertEquals(true, e.get(0).enters(v2));
        Assert.assertEquals(true, e.get(0).enters(v1));
        Assert.assertEquals(true, e.get(0).exits(v2));
        Assert.assertEquals(true, e.get(0).exits(v1));
        
    }
    
    public void testRemoveEdge_empty() {
        Vertex v1 = new Vertex("cows");
        Vertex v2 = new Vertex("cows");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);
        mManager.addEdge(v1, v2, VertexDirection.both, 0);
        mManager.applyOperation(new DeleteEdgeOperation(new Edge(v1, v2)));
        Assert.assertEquals(0, mManager.getEdgeDrawables().size());
    }
}
