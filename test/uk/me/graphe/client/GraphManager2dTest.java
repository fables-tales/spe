package uk.me.graphe.client;

import java.util.Collection;

import uk.me.graphe.client.EdgeDrawable;
import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.graphmanagers.GraphManager2d;
import uk.me.graphe.graphmanagers.GraphManager2dFactory;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * NOTE: this class relies on the GraphManager2d factory working, if it isn't
 * you should probably assume that this test is totally broken
 */
public class GraphManager2dTest extends TestCase {
    private GraphManager2d mManager;
    private boolean mCalled = false;
    private Runnable mCheckCalled = new Runnable() {

        @Override
        public void run() {
            GraphManager2dTest.this.mCalled = true;

        }
    };

    /**
     * runs test setup, creates a graphmanager instance
     */
    public void setUp() {
        mManager = GraphManager2dFactory.getInstance().makeDefaultGraphManager();
        mCalled = false;
    }

    public void tearDown() {
        mManager = null;
        mCalled = false;
    }

    /**
     * tests that adding vertices works
     */
    public void testAddVertex_single() {
        mManager.addVertex(new Vertex("hi"), 0, -3, 10);
        Graph underlying = mManager.getUnderlyingGraph();
        Assert.assertEquals(true, underlying.getVertices().contains(new Vertex("hi")));
        Collection<VertexDrawable> drawables = mManager.getVertexDrawables();
        Assert.assertEquals(1, drawables.size());
        VertexDrawable[] vds = drawables.toArray(new VertexDrawable[] { new VertexDrawable(0, 0, 0,
                0, "") });
        Assert.assertEquals(0, vds[0].getCenterX());
        Assert.assertEquals(-5, vds[0].getLeft());
        Assert.assertEquals(-3, vds[0].getCenterY());
        Assert.assertEquals(-8, vds[0].getTop());
        Assert.assertEquals(10, vds[0].getWidth());
        Assert.assertEquals(10, vds[0].getHeight());
        Assert.assertEquals("hi", vds[0].getLabel());
    }
    
    public void testRemoveVertex_single_noEdges() {
        mManager.addVertex(new Vertex("bees"), 0, 0, 10);
        mManager.removeVertex(new Vertex("bees"));
        Assert.assertEquals(false, mManager.getUnderlyingGraph().getVertices().contains(new Vertex("bees")));
    }
    
    public void testRemoveVertex_double_edges() {
        Vertex v1 = new Vertex("bees");
        Vertex v2 = new Vertex("cheese");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);
        mManager.addEdge(v1, v2, VertexDirection.both);
        mManager.removeVertex(new Vertex("bees"));
        Assert.assertEquals(false, mManager.getUnderlyingGraph().getVertices().contains(new Vertex("bees")));
        Assert.assertEquals(0, mManager.getUnderlyingGraph().getEdges().size());
    }
    
    public void testRemoveVertex_triple_line() {
        Vertex v1 = new Vertex("bees");
        Vertex v2 = new Vertex("cheese");
        Vertex v3 = new Vertex("cake");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);
        mManager.addVertex(v3, 0, 0, 10);
        mManager.addEdge(v1, v2, VertexDirection.both);
        mManager.addEdge(v2, v3, VertexDirection.both);
        mManager.removeVertex(v3);
        Assert.assertEquals(false, mManager.getUnderlyingGraph().getVertices().contains(v3));
        for (Edge e : mManager.getUnderlyingGraph().getEdges()) {
            Assert.assertEquals(false, e.enters(v3) || e.exits(v3));
        }
    }
    
    public void testRemoveVertex_triple_complete() {
        Vertex v1 = new Vertex("bees");
        Vertex v2 = new Vertex("cheese");
        Vertex v3 = new Vertex("cake");
        mManager.addVertex(v1, 0, 0, 10);
        mManager.addVertex(v2, 0, 0, 10);
        mManager.addVertex(v3, 0, 0, 10);
        mManager.addEdge(v1, v2, VertexDirection.both);
        mManager.addEdge(v2, v3, VertexDirection.both);
        mManager.addEdge(v1, v3, VertexDirection.both);
        mManager.removeVertex(v3);
        Assert.assertEquals(false, mManager.getUnderlyingGraph().getVertices().contains(v3));
        for (Edge e : mManager.getUnderlyingGraph().getEdges()) {
            Assert.assertEquals(false, e.enters(v3) || e.exits(v3));
        }
    }

    public void testAddEdge_singleBothDir() {
        mManager.addVertex(new Vertex("cake"), 0, -1, 0);
        mManager.addVertex(new Vertex("faces"), 100, 17, 0);
        mManager.addEdge(new Vertex("faces"), new Vertex("cake"), VertexDirection.both);
        EdgeDrawable e = mManager.getEdgeDrawables().toArray(new EdgeDrawable[0])[0];
        Assert.assertEquals(0, e.getStartX());
        Assert.assertEquals(-1, e.getStartY());
        Assert.assertEquals(100, e.getEndX());
        Assert.assertEquals(17, e.getEndY());
    }

    public void testAddVertex_multiple() {
        mManager.addVertex(new Vertex("bees"), 100, 17, 2);
        mManager.addVertex(new Vertex("cheese"), 0, 0, 2);

        Graph underlying = mManager.getUnderlyingGraph();
        Assert.assertEquals(2, underlying.getVertices().size());
        Assert.assertEquals(true, underlying.getVertices().contains(new Vertex("bees")));
        Assert.assertEquals(true, underlying.getVertices().contains(new Vertex("cheese")));

        VertexDrawable[] vds = mManager.getVertexDrawables().toArray(new VertexDrawable[] {});
        Assert.assertEquals(2, vds.length);
    }

    public void testInvalidate_addEdge() {
        mManager.addRedrawCallback(mCheckCalled);
        mManager.addVertex(new Vertex("a"), 0, 0, 10);
        mManager.addVertex(new Vertex("b"), 3, 7, 10);
        mManager.addEdge(new Vertex("a"), new Vertex("b"), VertexDirection.fromTo);
        Assert.assertEquals(true, mCalled);
    }

    public void testInvalidate_addVertex() {
        mManager.addRedrawCallback(mCheckCalled);
        mManager.addVertex(new Vertex("bees"), 0, 0, 10);
        Assert.assertEquals(true, mCalled);
    }

    public void testInvalidate_removeVertex() {
        mManager.addVertex(new Vertex("faces"), 0, 0, 10);
        mManager.addRedrawCallback(mCheckCalled);
        mManager.removeVertex(new Vertex("faces"));
        Assert.assertEquals(true, mCalled);
    }

    public void testInvalidate_removeAllEdges() {
        mManager.addVertex(new Vertex("faces"), 0, 0, 10);
        mManager.addVertex(new Vertex("bees"), 0, 0, 10);
        mManager.addEdge(new Vertex("bees"), new Vertex("faces"), VertexDirection.both);
        mManager.addRedrawCallback(mCheckCalled);
        mManager.removeAllEdges(new Vertex("faces"), new Vertex("bees"));
        Assert.assertEquals(true, mCalled);
    }
}
