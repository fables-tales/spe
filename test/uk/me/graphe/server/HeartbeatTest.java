package uk.me.graphe.server;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.me.graphe.shared.messages.HeartbeatMessage;
import uk.me.graphe.shared.messages.Message;

public class HeartbeatTest extends TestCase {
    
    private TestClient mTestClient;
    
    @Override
    public void setUp() {
        mTestClient = new TestClient();
        mTestClient.bringUpServer();
        mTestClient.connect();
    }
    
    public void testHeartbeat() {
        mTestClient.sendMessage(new HeartbeatMessage());
        Message m = mTestClient.readNextMessage();
        Assert.assertEquals("heartbeat", m.getMessage());
    }
    
    @Override
    public void tearDown() {
        mTestClient = null;
    }
}
