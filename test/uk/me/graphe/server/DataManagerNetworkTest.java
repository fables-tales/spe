package uk.me.graphe.server;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.graphmanagers.OTGraphManager2d;
import uk.me.graphe.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.server.messages.MakeGraphMessage;
import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.NoSuchGraphMessage;
import uk.me.graphe.server.messages.OpenGraphMessage;
import uk.me.graphe.server.messages.RequestGraphMessage;
import uk.me.graphe.server.messages.StateIdMessage;
import uk.me.graphe.server.messages.operations.AddNodeOperation;
import uk.me.graphe.server.messages.operations.CompositeOperation;
import uk.me.graphe.server.messages.operations.GraphOperation;
import uk.me.graphe.shared.Vertex;

public class DataManagerNetworkTest extends TestCase {

    private TestClient mClient = new TestClient();
    
    @Override
    protected void setUp() throws Exception {
       mClient = new TestClient();
       mClient.bringUpServer();
       mClient.connect();
       System.out.println("new test");
       
    }
    
    
    public void testCreateGraph() {
        mClient.sendMessage(new MakeGraphMessage());
        Message m = mClient.readNextMessage();
        Assert.assertEquals("openGraph", m.getMessage());
        OpenGraphMessage ogr = (OpenGraphMessage)m;
        Assert.assertNotNull(DataManager.getGraph(ogr.getId()));
        
        //we should get a state id message after this
        m = mClient.readNextMessage();
        Assert.assertEquals(new StateIdMessage(0, 0).getMessage(), m.getMessage());
        StateIdMessage sim = (StateIdMessage)m;
        Assert.assertEquals(ogr.getId(), sim.getGraphId());
    }
    
    public void testRequestGraph_nograph() {
        mClient.sendMessage(new RequestGraphMessage(73, 14));
        Message m = mClient.readNextMessage();
        Assert.assertEquals(new NoSuchGraphMessage().getMessage(), m.getMessage());
    }
    
    public void testRequestGraph_suchgraph() {
        int gKey = DataManager.create();
        OTGraphManager2d g = DataManager.getGraph(gKey);
        g.applyOperation(new AddNodeOperation(new Vertex("bees"), 0, 0));
        g.applyOperation(new AddNodeOperation(new Vertex("faces"), 0, 0));
        DataManager.save(g);
        mClient.sendMessage(new RequestGraphMessage(g.getGraphId(), 0));
        Message m = mClient.readNextMessage();
        Assert.assertEquals("composite", m.getMessage());
        List<GraphOperation> operations = ((CompositeOperation)m).asIndividualOperations();
        Assert.assertEquals(2, operations.size());
        OTGraphManager2d cliGraph = OTGraphManagerFactory.newInstance(gKey);
        cliGraph.applyOperation(operations.get(0));
        cliGraph.applyOperation(operations.get(1));
        Assert.assertEquals(cliGraph.getStateId(), g.getStateId());
    }
    
    public void testAddNode() {
        mClient.sendMessage(new MakeGraphMessage());
        Message m = mClient.readNextMessage();
        OpenGraphMessage ogr = (OpenGraphMessage)m;
        OTGraphManager2d g = OTGraphManagerFactory.newInstance(ogr.getId());
        m = mClient.readNextMessage();
        Assert.assertEquals("updateStateId", m.getMessage());
        StateIdMessage sm = (StateIdMessage)m;
        Assert.assertEquals(0, sm.getState());
        g.addVertex(new Vertex("faces"), 37, 14, 0);
        OTGraphManager2d gServer = DataManager.getGraph(ogr.getId());
        Assert.assertEquals(0, gServer.getUnderlyingGraph().getVertices().size());
        mClient.sendMessage(new AddNodeOperation(new Vertex("faces"), 37, 14));
        m = mClient.readNextMessage();
        System.err.println(m.getMessage());
    }
    
    @Override
    protected void tearDown() throws Exception {
        DataManager.flush();
        mClient = null;
    }

}
