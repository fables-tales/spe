package uk.me.graphe.shared;

import uk.me.graphe.graphmanagers.OTGraphManager2d;
import uk.me.graphe.graphmanagers.OTGraphManagerFactory;
import junit.framework.Assert;
import junit.framework.TestCase;

public class OTGraphManagerFactoryTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testFactory() {
        
        OTGraphManager2d manager = OTGraphManagerFactory.newInstance(3);
        Assert.assertEquals(3, manager.getGraphId());
        Assert.assertEquals(0, manager.getStateId());
    }

}
