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

    public void testBothEdgeImplicit() {
        Edge e1 = new Edge(mV1, mV2);
        Assert.assertEquals(true, e1.exits(mV1));
        Assert.assertEquals(true, e1.enters(mV2));
        Assert.assertEquals(true, e1.exits(mV2));
        Assert.assertEquals(true, e1.enters(mV1));
        Assert.assertEquals(false, e1.enters(mV3));
        Assert.assertEquals(false, e1.exits(mV3));
    }
    
    public void testBothEdgeExplicit() {
        Edge e1 = new Edge(mV2, mV3, VertexDirection.both);
        Assert.assertEquals(true, e1.exits(mV2));
        Assert.assertEquals(true, e1.enters(mV3));
        Assert.assertEquals(true, e1.exits(mV3));
        Assert.assertEquals(true, e1.enters(mV2));
        Assert.assertEquals(false, e1.enters(mV1));
        Assert.assertEquals(false, e1.exits(mV1));
    }

}