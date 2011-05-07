package uk.me.graphe.shared;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.graphmanagers.OTStyleGraphManager2d;

public class OTGraphManagerFactoryTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testFactory() {
        
        OTStyleGraphManager2d manager = OTGraphManagerFactory.newInstance(3);
        Assert.assertEquals(3, manager.getGraphId());
        Assert.assertEquals(0, manager.getStateId());
    }

}
