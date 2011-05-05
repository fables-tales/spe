package uk.me.graphe.server;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.server.database.UserDatabase;
import uk.me.graphe.server.org.json.wrapper.JSONWrapperFactory;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;

public class UserTest extends TestCase {

    private UserDatabase db = new UserDatabase();
    private OTGraphManager2d mManager;
    private int graphId = 0;

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        JSONImplHolder.initialise(new JSONWrapperFactory());
        mManager = OTGraphManagerFactory.newInstance(17);
    }

    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
        mManager = null;
    }

    public void testUser() {
        db.newUser("test", "email@example.com");
        db.newUser("test2", "email@example.com");
        db.addGraph("test", "aGraph");
        String key = "graphKey";
        db.setGraphsToUsers("email@example.com", key);
        Assert.assertEquals(db.getEmailFromId("test"), "email@example.com");
        List<String> graphs = db.getGraphs("test");
        List<String> graphs2 = db.getGraphs("test2");
        Assert.assertEquals(graphs.get(1), key);
        Assert.assertEquals(graphs2.get(0), key);
        db.deleteUser("test");
        db.deleteUser("test2");
    	
    }
    
    public void storeGraph() {
    	db.newUser("graphtest", "email@example.com");
    	db.addGraph("graphtest", "key");
    	List<String> graphs = db.getGraphs("graphtest");
    	Assert.assertEquals(1, graphs.size());
    	Assert.assertEquals(graphs.get(0), "key");
    	db.deleteUser("graphtest");
    }
}

