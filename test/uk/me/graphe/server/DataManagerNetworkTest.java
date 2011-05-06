package uk.me.graphe.server;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.client.EdgeDrawable;
import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.graphmanagers.OTStyleGraphManager2d;
import uk.me.graphe.shared.messages.MakeGraphMessage;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.NoSuchGraphMessage;
import uk.me.graphe.shared.messages.OpenGraphMessage;
import uk.me.graphe.shared.messages.RequestGraphMessage;
import uk.me.graphe.shared.messages.StateIdMessage;
import uk.me.graphe.shared.messages.operations.AddEdgeOperation;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;



public class DataManagerNetworkTest extends TestCase {

    private TestClient mClient = new TestClient();
    private TestClient mClient2 = new TestClient();
    
    @Override
    protected void setUp() throws Exception {
       DataManager.flush();
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
        assertNullCompositeOperation();
        m = mClient.readNextMessage();
        Assert.assertEquals("updateStateId", m.getMessage());
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
        OTStyleGraphManager2d g = DataManager.getGraph(gKey);
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
    
    public void testAddNode_nostate() {
        mClient.sendMessage(new MakeGraphMessage());
        Message m = mClient.readNextMessage();
        OpenGraphMessage ogr = (OpenGraphMessage)m;
        OTGraphManager2d g = OTGraphManagerFactory.newInstance(ogr.getId());
        
        //pump off the composite operation
        assertNullCompositeOperation();
        
        
        m = mClient.readNextMessage();
        Assert.assertEquals("updateStateId", m.getMessage());
        StateIdMessage sm = (StateIdMessage)m;
        Assert.assertEquals(0, sm.getState());
        g.addVertex(new Vertex("faces"), 37, 14, 0);
        OTGraphManager2d gServer = DataManager.getGraph(ogr.getId());
        Assert.assertEquals(0, gServer.getUnderlyingGraph().getVertices().size());
        mClient.sendMessage(new AddNodeOperation(new Vertex("faces"), 37, 14));
        
        //pump off the composite operation
        assertNullCompositeOperation();
        
        m = mClient.readNextMessage();
        Assert.assertEquals("updateStateId", m.getMessage());
        Assert.assertEquals(1, ((StateIdMessage)m).getState());
        
    }
    
    public void testAddNode_somestate() {
        mClient.sendMessage(new MakeGraphMessage());
        Message m = mClient.readNextMessage();
        OpenGraphMessage ogr = (OpenGraphMessage)m;
        OTGraphManager2d g = OTGraphManagerFactory.newInstance(ogr.getId());
        assertNullCompositeOperation();
        
        m = mClient.readNextMessage();
        Assert.assertEquals("updateStateId", m.getMessage());
        StateIdMessage sim = (StateIdMessage)m;
        Assert.assertEquals(ogr.getId(), sim.getGraphId());
        
        OTGraphManager2d serverGraph = DataManager.getGraph(ogr.getId());
        serverGraph.applyOperation(new AddNodeOperation(new Vertex("faces"), 37, 84));
        mClient.sendMessage(new RequestGraphMessage(ogr.getId(), sim.getState()));
        m = mClient.readNextMessage();
        Assert.assertEquals("composite", m.getMessage());
        CompositeOperation co = (CompositeOperation)m;
        
        for (GraphOperation o : co.asIndividualOperations()) {
            g.applyOperation(o);
        }
        
        Assert.assertEquals(g.getStateId(), serverGraph.getStateId());
        Graph underLyingCli = g.getUnderlyingGraph();
        Graph underLyingSrv = serverGraph.getUnderlyingGraph();
        Assert.assertEquals(underLyingCli.getVertices(), underLyingSrv.getVertices());
        
    }
    
    public void testAddEdge_noEdges_bidirectional() {
        VertexDirection vd = VertexDirection.both;
        EdgeDrawable ed = makeServerEdge(vd);
        Assert.assertFalse(ed.needsFromToArrow());
        Assert.assertFalse(ed.needsToFromArrow());
    }
    
    public void testAddEdge_noEdges_fromTo() {
        VertexDirection vd = VertexDirection.fromTo;
        EdgeDrawable ed = makeServerEdge(vd);
        Assert.assertTrue(ed.needsFromToArrow());
        Assert.assertFalse(ed.needsToFromArrow());
    }
    
    public void testAddEdge_noEdges_weight() {
        EdgeDrawable ed = makeServerEdge(VertexDirection.both, 12);
        Assert.assertEquals(12, ed.getWeight());
    }


    private EdgeDrawable makeServerEdge(VertexDirection vd) {
        return makeServerEdge(vd, 1);
    }
    
    private EdgeDrawable makeServerEdge(VertexDirection vd, int weight) {
        mClient.sendMessage(new MakeGraphMessage());
        Message m = mClient.readNextMessage();
        OpenGraphMessage ogr = (OpenGraphMessage)m;
        OTGraphManager2d g = OTGraphManagerFactory.newInstance(ogr.getId());
        assertNullCompositeOperation();
        
        m = mClient.readNextMessage();
        Assert.assertEquals("updateStateId", m.getMessage());
        StateIdMessage sim = (StateIdMessage)m;
        Assert.assertEquals(ogr.getId(), sim.getGraphId());
        
        OTGraphManager2d serverGraph = DataManager.getGraph(ogr.getId());
        Vertex a = new Vertex("bees");
        Vertex b = new Vertex("faces");
        
        AddNodeOperation ano1 = new AddNodeOperation(a, 3, 7);
        AddNodeOperation ano2 = new AddNodeOperation(b, 300, 700);
        g.applyOperation(ano1);
        g.applyOperation(ano2);
        Assert.assertEquals(2, g.getVertexDrawables().size());
        mClient.sendMessage(ano1);
        
        assertNullCompositeOperation();
        assertStateIdMessage(ogr.getId());
        mClient.sendMessage(ano2);
        assertNullCompositeOperation();
        assertStateIdMessage(ogr.getId());
        
        for (VertexDrawable vds : serverGraph.getVertexDrawables()) {
            System.err.println("thing: " + vds.getLabel());
        }
        
        Assert.assertEquals(2, serverGraph.getVertexDrawables().size());
        Edge e = new Edge(a,b,vd);
        e.setWeight(weight);
        Assert.assertEquals(weight, e.getWeight());
        AddEdgeOperation aeo = new AddEdgeOperation(e);
        g.applyOperation(aeo);
        System.err.println(aeo.toJson());
        mClient.sendMessage(aeo);
        assertNullCompositeOperation();
        assertStateIdMessage(ogr.getId());
        EdgeDrawable ed = (EdgeDrawable) serverGraph.getEdgeDrawables().toArray()[0];
        System.err.println("got ed:" + ed.getWeight());
        return ed;
    }


    private void assertStateIdMessage(int id) {
        Message m = mClient.readNextMessage();
        Assert.assertEquals(new StateIdMessage(0, 0).getMessage(), m.getMessage());
        Assert.assertEquals(id, ((StateIdMessage)m).getGraphId());
    }


    private void assertNullCompositeOperation() {
        Message m;
        m = mClient.readNextMessage();
        Assert.assertEquals("composite", m.getMessage());
        CompositeOperation co = (CompositeOperation)m;
        Assert.assertEquals(0, co.asIndividualOperations().size());
    }
    
    @Override
    protected void tearDown() throws Exception {
        DataManager.flush();
        mClient = null;
    }
    
    

}
