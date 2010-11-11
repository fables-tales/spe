package me.teaisaweso.client;

import junit.framework.Assert;
import junit.framework.TestCase;
import me.teaisaweso.client.graphmanagers.GraphManager2d;
import me.teaisaweso.client.graphmanagers.GraphManager2dFactory;
import me.teaisaweso.shared.Graph;
import me.teaisaweso.shared.Vertex;

/**
 * NOTE: this class relies on the GraphManager2d factory working, if it isn't
 * you should probably assume that this test is totally broken
 */
public class GraphManager2dTest extends TestCase {
	private GraphManager2d mManager;

	public void setUp() {
		mManager = GraphManager2dFactory.getInstance().makeDefaultGraphManager();
	}

	public void testAddVertex() {
		mManager.addVertex(new Vertex("hi"), 0, 0, 10);
		Graph underlying = mManager.getUnderlyingGraph();
		Assert.assertEquals(true, underlying.getVertices().contains(new Vertex("hi")));
	}
}
