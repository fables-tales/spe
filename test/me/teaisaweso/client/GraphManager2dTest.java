package me.teaisaweso.client;

import java.util.Collection;

import junit.framework.Assert;
import junit.framework.TestCase;
import me.teaisaweso.client.graphmanagers.GraphManager2d;
import me.teaisaweso.client.graphmanagers.GraphManager2dFactory;
import me.teaisaweso.shared.Graph;
import me.teaisaweso.shared.Vertex;
import me.teaisaweso.shared.VertexDirection;

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
        mManager.addEdge(new Vertex("a"), new Vertex("b"), VertexDirection.fromTo);
        Assert.assertEquals(true, mCalled);
    }

    public void testInvalidate_addVertex() {
        mManager.addRedrawCallback(mCheckCalled);
        mManager.addVertex(new Vertex("bees"), 0, 0, 10);
        Assert.assertEquals(true, mCalled);
    }

}
