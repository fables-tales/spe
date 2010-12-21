package uk.me.graphe.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientMessageHandler extends Thread {

    private ByteBuffer mBb = ByteBuffer.allocate(1024);
    private List<SocketChannel> mClientSockets = new ArrayList<SocketChannel>();
    private List<JSONObject> mIncomingMessages = new ArrayList<JSONObject>();
    // really really don't screw with this unless you know what you're doing
    private Selector mReadableSocketsSelector;

    private BlockingQueue<SocketChannel> mScQueue = new ArrayBlockingQueue<SocketChannel>(1024);

    private boolean mShutDown = false;

    public ClientMessageHandler() throws IOException {
        // why does this throw an io exception? I mean really
        mReadableSocketsSelector = Selector.open();
    }

    /**
     * adds a new client socketchannel to the system this way the client's
     * incoming messages will be read
     * 
     * @param sc
     */
    public void addClient(SocketChannel sc) {
        try {
            mScQueue.put(sc);
            mReadableSocketsSelector.wakeup();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    @Override
    public void run() {

        while (!mShutDown) {
            try {

                if (mClientSockets.size() > 0) {
                    getClientMessages(mIncomingMessages);
                    
                    
                    
                    // done with all the messages, clear them out
                    mIncomingMessages.clear();
                }

                // poll off items that got in before we finished the loop
                int items = mScQueue.size();

                for (int i = 0; i < items; i++) {
                    internalAddClient(mScQueue.poll());
                }

            } catch (IOException e) {
                throw new Error(e);
            }

        }

    }

    private void discconect(SelectionKey s, SocketChannel sc) {
        s.cancel();
        mClientSockets.remove(sc);
    }

    private void getClientMessages(List<JSONObject> m) throws IOException {
        mReadableSocketsSelector.select();
        for (SelectionKey s : mReadableSocketsSelector.selectedKeys()) {
            mBb.clear();

            // unsafe cast ahoy!
            // TODO: figure out how to make this not an unsafe cast
            SocketChannel sc = (SocketChannel) s.channel();
            if (sc.read(mBb) == -1) {
                discconect(s, sc);
            } else {
                String json = new String(mBb.array());
                boolean valid = this.validateAndParse(json, m);
                if (!valid) discconect(s, sc);
            }

        }
    }

    /**
     * adds a new client to the message handler
     * 
     * @param clientSock
     *            the socket of the client
     */
    private void internalAddClient(SocketChannel sc) {
        try {
            // it needs to be nonblocking for selectors to not explode
            sc.configureBlocking(false);
            sc.register(mReadableSocketsSelector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new Error(e);
        }

    }

    private boolean validateAndParse(String json, List<JSONObject> inc) {
        int start = 0;
        int end = -1;
        do {
            end = json.indexOf("\0", end);

            // check for a malformed message
            if (json.charAt(end - 1) != '}') return false;

            try {
                JSONObject jso = new JSONObject(json.substring(start, end - 1));
                inc.add(jso);
                start = end + 1;
            } catch (JSONException e) {
                // swallow the exception, we can deal with a bad format
                return false;
            }

        } while (end != -1);

        return true;
    }

}
