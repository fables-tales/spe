package uk.me.graphe.server;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * a class to represent the state of a client on the server
 * 
 * holds a key to the representation of the current graph it is dealing with
 * at the current time
 * @author Sam Phippen <samphippen@googlemail.com>
 *
 */
public class Client {

    private SocketChannel mChannel;
    private GraphKey mCurrentGraph;
    private ByteBuffer mReadBuffer = ByteBuffer.allocate(1024);
    private boolean mConnected = true;
    
    public Client(SocketChannel clientSock) {
        mChannel = clientSock;
    }

    public SocketChannel getChannel() {
        return mChannel;
    }
    
    /**
     * possible side effect of disconnecting the client
     * @return the next messages from this client
     */
    public List<String> readNextMessages() {
    	return null;
    }
    
    public boolean isConnected() {
    	return mConnected;
    }

}
