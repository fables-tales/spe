package uk.me.graphe.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import uk.me.graphe.server.operations.HeartbeatOp;
import uk.me.graphe.server.operations.Operation;

import com.google.gwt.dev.util.Pair;



public class ClientMessageSender extends Thread {
    private static ClientMessageSender sInstance = null;
    
    private Operation mHeartbeat = new HeartbeatOp(); 
    
    private BlockingQueue<Pair<Client, Operation>> mQueue = new ArrayBlockingQueue<Pair<Client,Operation>>(1024);

    private boolean mShutDown;
    
    public static ClientMessageSender getInstance() {
        if (sInstance == null) sInstance = new ClientMessageSender();
        return sInstance;
    }

    public void sendHeartbeat(Client c) throws InterruptedException {
        mQueue.put(Pair.create(c, mHeartbeat));
    }
    
    
    @Override
    public void run() {
        while (!mShutDown) {
            try {
				Pair<Client, Operation> clOp = mQueue.take();
				String s = clOp.right.toJson() + "\0";
				Client c = clOp.left;
				if (c.isConnected()) clOp.left.getChannel().write(ByteBuffer.wrap(s.getBytes()));
			} catch (InterruptedException e) {
				throw new Error(e);
			} catch (IOException ioe) {
				throw new Error(ioe);
			}
        }
        
    }
    
}
