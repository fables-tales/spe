package me.teaisaweso.shared;

import junit.framework.Assert;
import junit.framework.TestCase;

public class EdgeTest extends TestCase {

	private Vertex mV1, mV2, mV3;

	public void setUp() throws Exception {
		mV1 = new Vertex("a");
		mV2 = new Vertex("b");
		mV3 = new Vertex("c");
	}

	/**
	 * tests that a both edge has the right properties with the implicit
	 * constructor
	 */
	public void testBothEdgeImplicit() {
		Edge e1 = new Edge(mV1, mV2);
		Assert.assertEquals(true, e1.exits(mV1));
		Assert.assertEquals(true, e1.enters(mV2));
		Assert.assertEquals(true, e1.exits(mV2));
		Assert.assertEquals(true, e1.enters(mV1));
		Assert.assertEquals(false, e1.enters(mV3));
		Assert.assertEquals(false, e1.exits(mV3));
	}

	/**
	 * tests that a both edge has the right properties with an explict
	 * constructor
	 */
	public void testBothEdgeExplicit() {
		Edge e1 = new Edge(mV2, mV3, VertexDirection.both);
		Assert.assertEquals(true, e1.exits(mV2));
		Assert.assertEquals(true, e1.enters(mV3));
		Assert.assertEquals(true, e1.exits(mV3));
		Assert.assertEquals(true, e1.enters(mV2));
		Assert.assertEquals(false, e1.enters(mV1));
		Assert.assertEquals(false, e1.exits(mV1));
	}

	/**
	 * tests a from-to edge has the right properties with an explicit
	 * constructor
	 */
	public void testFromToEdgeExplicit() {
		Edge e1 = new Edge(mV2, mV1, VertexDirection.fromTo);
		Assert.assertEquals(true, e1.exits(mV2));
		Assert.assertEquals(true, e1.enters(mV1));

		Assert.assertEquals(false, e1.exits(mV1));
		Assert.assertEquals(false, e1.enters(mV2));

		Assert.assertEquals(false, e1.enters(mV3));
		Assert.assertEquals(false, e1.exits(mV3));
	}

	/**
	 * tests a to-from edge has the right properties with an explicit
	 * constructor
	 */
	public void testToFromEdgeExplicit() {
		Edge e1 = new Edge(mV2, mV1, VertexDirection.toFrom);
		Assert.assertEquals(true, e1.exits(mV1));
		Assert.assertEquals(true, e1.enters(mV2));

		Assert.assertEquals(false, e1.exits(mV2));
		Assert.assertEquals(false, e1.enters(mV1));

		Assert.assertEquals(false, e1.enters(mV3));
		Assert.assertEquals(false, e1.exits(mV3));
	}
	
	/**
	 * tests that from vertex works
	 */
	public void testGetFromVertex() {
		Edge e1 = new Edge(mV1, mV2);
		Assert.assertEquals(mV1, e1.getFromVertex());
		e1 = new Edge(mV2, mV1);
		Assert.assertEquals(mV2, e1.getFromVertex());
		e1 = new Edge(mV3, mV3);
		Assert.assertEquals(mV3, e1.getFromVertex());
	}
	
	/**
	 * tests that to vertex works
	 */
	public void testGetToVertex() {
		Edge e1 = new Edge(mV1, mV3);
		Assert.assertEquals(mV3, e1.getToVertex());
		e1 = new Edge(mV1, mV2);
		Assert.assertEquals(mV2, e1.getToVertex());
		e1 = new Edge(mV1, mV1);
		Assert.assertEquals(mV1, e1.getToVertex());
	}
	
	/**
	 * tests the edge direction method
	 */
	public void testEdgeDirection() {
		Edge e1 = new Edge(mV1, mV2);
		Assert.assertEquals(false, e1.hasDirection());
		e1 = new Edge(mV1, mV1);
		Assert.assertEquals(false, e1.hasDirection());
		e1 = new Edge(mV1, mV1, VertexDirection.fromTo);
		Assert.assertEquals(true, e1.hasDirection());
		e1 = new Edge(mV1, mV2, VertexDirection.fromTo);
		Assert.assertEquals(true, e1.hasDirection());
		e1 = new Edge(mV3, mV1, VertexDirection.toFrom);
		Assert.assertEquals(true, e1.hasDirection());
		e1 = new Edge(mV3, mV2, VertexDirection.toFrom);
		Assert.assertEquals(true, e1.hasDirection());
	}
}
