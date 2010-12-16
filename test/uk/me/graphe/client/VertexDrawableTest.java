package uk.me.graphe.client;

import java.util.Random;

import uk.me.graphe.client.VertexDrawable;

import junit.framework.Assert;
import junit.framework.TestCase;

public class VertexDrawableTest extends TestCase {
    private static int[] sTestValues = new int[] { 0, 14, 317, -4, Integer.MAX_VALUE,
            Integer.MIN_VALUE };

    private static String[] sTestLabels = new String[] { "bees", "faces", "cake", "death", "cheese" };

    private static Random sRand = new Random();

    /**
     * tests that getleft works as it should
     */
    public void testLeft() {
        for (int i : sTestValues) {
            VertexDrawable vd = new VertexDrawable(i, sRand.nextInt(), sRand.nextInt(), sRand
                    .nextInt(), "");
            Assert.assertEquals(i, vd.getLeft());
        }
    }

    /**
     * tests that getTop works as it should
     */
    public void testTop() {
        for (int i : sTestValues) {
            VertexDrawable vd = new VertexDrawable(sRand.nextInt(), i, sRand.nextInt(), sRand
                    .nextInt(), "");
            Assert.assertEquals(i, vd.getTop());
        }
    }

    /**
     * tests that getWidth works as it should
     */
    public void testWidth() {
        for (int i : sTestValues) {
            VertexDrawable vd = new VertexDrawable(sRand.nextInt(), sRand.nextInt(), i, sRand
                    .nextInt(), "");
            Assert.assertEquals(i, vd.getWidth());
        }
    }

    /**
     * tests that getHeight works as it should
     */
    public void testHeight() {
        for (int i : sTestValues) {
            VertexDrawable vd = new VertexDrawable(sRand.nextInt(), sRand.nextInt(), sRand
                    .nextInt(), i, "");
            Assert.assertEquals(i, vd.getHeight());
        }
    }

    /**
     * tests that getlabel works as it should
     */
    public void testLabel() {
        for (String s : sTestLabels) {
            VertexDrawable vd = new VertexDrawable(sRand.nextInt(), sRand.nextInt(), sRand
                    .nextInt(), sRand.nextInt(), s);
            Assert.assertEquals(s, vd.getLabel());
        }
    }

}
