package uk.me.graphe.server;

import java.nio.channels.SocketChannel;

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
    
    public Client(SocketChannel clientSock) {
        mChannel = clientSock;
    }

    public SocketChannel getChannel() {
        return mChannel;
    }

}
