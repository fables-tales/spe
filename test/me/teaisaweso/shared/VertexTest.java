package me.teaisaweso.shared;

import junit.framework.TestCase;
import junit.framework.Assert;

public class VertexTest extends TestCase {

    static String[] sTestLabels = new String[] { "bees", "cows", "potatoes", "cake", "death" };

    /**
     * tests that vertex labeling works as expected
     */
    public void testVertexLabels() {
        for (String s : sTestLabels) {
            Vertex v = new Vertex(s);
            Assert.assertEquals(s, v.getLabel());
        }

    }

    /**
     * test that vertex equality works and is reflexive (if a == b then b == a)
     */
    public void testVertexEquality() {
        for (String s1 : sTestLabels) {
            for (String s2 : sTestLabels) {
                boolean assertCheck = false;
                if (s1.equals(s2)) assertCheck = true;

                Assert.assertEquals(assertCheck, new Vertex(s1).equals(new Vertex(s2)));
                Assert.assertEquals(assertCheck, new Vertex(s2).equals(new Vertex(s1)));

            }

        }

    }

}
