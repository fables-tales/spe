package uk.me.graphe.client;

import java.util.Random;

import uk.me.graphe.shared.VertexDirection;

import junit.framework.Assert;
import junit.framework.TestCase;

public class EdgeDrawableTest extends TestCase {
    static int[] sTestValues = new int[] { 0, -10, 7, Integer.MAX_VALUE, Integer.MIN_VALUE };
    static Random sRand = new Random();

    /**
     * tests that start x works as expected
     */
    public void testStartX() {
        for (int i : sTestValues) {
            EdgeDrawable ed = new EdgeDrawable(i, sRand.nextInt(), sRand.nextInt(), sRand.nextInt());
            Assert.assertEquals(i, ed.getStartX());
        }
    }

    /**
     * tests that start y works as expected
     */
    public void testStartY() {
        for (int i : sTestValues) {
            EdgeDrawable ed = new EdgeDrawable(sRand.nextInt(), i, sRand.nextInt(), sRand.nextInt());
            Assert.assertEquals(i, ed.getStartY());
        }
    }

    /**
     * tests that end x works as expected
     */
    public void testEndX() {
        for (int i : sTestValues) {
            EdgeDrawable ed = new EdgeDrawable(sRand.nextInt(), sRand.nextInt(), i, sRand.nextInt());
            Assert.assertEquals(i, ed.getEndX());
        }
    }

    /**
     * tests that end y works as expected
     */
    public void testEndY() {
        for (int i : sTestValues) {
            EdgeDrawable ed = new EdgeDrawable(sRand.nextInt(), sRand.nextInt(), sRand.nextInt(), i);
            Assert.assertEquals(i, ed.getEndY());
        }
    }

    /**
     * tests the use of needsarrow in an edge drawable
     */
    public void testArrow() {
        EdgeDrawable e = new EdgeDrawable(sRand.nextInt(), sRand.nextInt(), sRand.nextInt(), sRand
                .nextInt());
        Assert.assertEquals(false, e.needsFromToArrow());
        Assert.assertEquals(false, e.needsToFromArrow());
        e = new EdgeDrawable(sRand.nextInt(), sRand.nextInt(), sRand.nextInt(), sRand.nextInt(),
                VertexDirection.both);
        Assert.assertEquals(false, e.needsFromToArrow());
        Assert.assertEquals(false, e.needsToFromArrow());
        e = new EdgeDrawable(sRand.nextInt(), sRand.nextInt(), sRand.nextInt(), sRand.nextInt(),
                VertexDirection.fromTo);
        Assert.assertEquals(true, e.needsFromToArrow());
        Assert.assertEquals(false, e.needsToFromArrow());
        e = new EdgeDrawable(sRand.nextInt(), sRand.nextInt(), sRand.nextInt(), sRand.nextInt(),
                VertexDirection.toFrom);
        Assert.assertEquals(false, e.needsFromToArrow());
        Assert.assertEquals(true, e.needsToFromArrow());
    }
}
