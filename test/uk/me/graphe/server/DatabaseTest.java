package uk.me.graphe.server;

import junit.framework.TestCase;
import uk.me.graphe.server.database.Database;
import uk.me.graphe.server.database.DatabaseFactory;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.messages.operations.AddEdgeOperation;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;


public class DatabaseTest extends TestCase{

    private Database db = DatabaseFactory.newInstance();
    private OTGraphManager2d mManager;
    private int graphId = 0;
    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
        mManager = OTGraphManagerFactory.newInstance(17);
    }

    @Override
    protected void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
        mManager = null;
    }
    
    public void testStore () {
        Vertex v1 = new Vertex("v1");
        Vertex v2 = new Vertex("v2");
        mManager.applyOperation(new AddNodeOperation(v1, 1, 1));
        mManager.applyOperation(new AddNodeOperation(v2, 1, 4));
        mManager.applyOperation(new AddEdgeOperation(new Edge(v1,v2)));
        graphId = db.store(mManager);   
    }
    
    public void testRetrieve() {
        //OTGraphManager2d retrieve = db.retrieve(graphId);
        
    }
}
