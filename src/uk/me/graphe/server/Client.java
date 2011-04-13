package uk.me.graphe.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * a class to represent the state of a client on the server
 * 
 * holds a key to the representation of the current graph it is dealing with at
 * the current time
 * 
 * @author Sam Phippen <samphippen@googlemail.com>
 * 
 */
public class Client {

    private SocketChannel mChannel;
    private ByteBuffer mReadBuffer = ByteBuffer.allocate(1024);
    private boolean mConnected = true;
    private int mSelectedGraphId = -1;
    private int mStateId;

    public Client(SocketChannel clientSock) {
        mChannel = clientSock;
    }

    public SocketChannel getChannel() {
        return mChannel;
    }

    /**
     * possible side effect of disconnecting the client
     * 
     * @return the next messages from this client
     */
    public List<String> readNextMessages() {
        try {
            int read = mChannel.read(mReadBuffer);
            if (read == -1) {
                ClientManager.getInstance().disconnect(this);
                return null;
            } else {
                // get messages
                StringBuilder sb = new StringBuilder();
                while (read == 1024) {
                    sb.append(new String(mReadBuffer.array()));
                    mReadBuffer.clear();
                    read = mChannel.read(mReadBuffer);
                }

                sb.append(new String(mReadBuffer.array(), 0, read));
                System.err.println(sb.length());
                System.err.println("ponies: " + sb.toString());
                mReadBuffer.clear();
                return processMessages(sb);
            }

        } catch (IOException e) {
            throw new Error(e);
        }

    }

    private List<String> processMessages(StringBuilder sb) {
        int start = 0;
        List<String> result = new ArrayList<String>();
        System.err.println(sb.toString());
        System.err.println(sb.length());
        while (start < sb.length()) {
            int nullIndex = sb.indexOf("\0", start);
            if (nullIndex == -1) nullIndex = sb.indexOf("u\0000", start);
            if (nullIndex == -1) nullIndex = sb.length();
            String ss = sb.substring(start, nullIndex);
            result.add(ss);
            start = nullIndex + 1;
        }

        return result;
    }

    public boolean isConnected() {
        return mConnected;
    }

    public void disconnect() {
        mConnected = false;
    }

    public int getCurrentGraphId() {
        return mSelectedGraphId;
    }

    public void setCurrentGraphId(int id) {
        if (mSelectedGraphId != -1) {
            ClientManager.getInstance().removeClientGraph(id, this);
        }
        mSelectedGraphId = id;
        ClientManager.getInstance().setClientGraph(this, id);
    }

    public int getCurrentStateId() {
        return mStateId;
    }

    public void updateStateId(int stateId) {
        mStateId = stateId;
    }

}
