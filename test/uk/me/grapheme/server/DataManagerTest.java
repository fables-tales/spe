package uk.me.grapheme.server;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.graphmanagers.OTGraphManager2d;
import uk.me.graphe.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.server.DataManager;
import uk.me.graphe.server.messages.operations.NoOperation;

public class DataManagerTest extends TestCase {
    public void testCreate_single() {
        int key = DataManager.create();
        OTGraphManager2d manager = DataManager.getGraph(key);
        Assert.assertEquals(key, manager.getGraphId());
        Assert.assertEquals(0, manager.getStateId());
    }
    
    public void testCreate_multiple() {
        int key = DataManager.create();
        int key2 = DataManager.create();
        OTGraphManager2d manager = DataManager.getGraph(key);
        OTGraphManager2d manager2 = DataManager.getGraph(key2);
        Assert.assertNotSame(manager, manager2);
    }
    
    public void testSave() {
        int key = DataManager.create();
        OTGraphManager2d manager = OTGraphManagerFactory.newInstance(key);
        manager.applyOperation(new NoOperation());
        Assert.assertNotSame(DataManager.getGraph(key), manager);
        DataManager.save(manager);
        OTGraphManager2d manager2 = DataManager.getGraph(key);
        Assert.assertEquals(1, manager2.getStateId());
    }
    
    public void testGet() {
        this.testCreate_single();
        
    }
}
