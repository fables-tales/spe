package me.teaisaweso.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientMessageHandler extends Thread {

    private List<SocketChannel> mClientSockets = new ArrayList<SocketChannel>();
    private boolean mShutDown = false;
    // really really don't screw with this unless you know what you're doing
    private Selector mReadableSocketsSelector;
    private BlockingQueue<SocketChannel> mScQueue = new ArrayBlockingQueue<SocketChannel>(1024);

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

    private static ByteBuffer sBb = ByteBuffer.allocate(1024);
    
    @Override
    public void run() {
        
        while (!mShutDown) {
            try {

                if (mClientSockets.size() > 0) {
                    mReadableSocketsSelector.select();
                    for (SelectionKey s : mReadableSocketsSelector.selectedKeys()) {
                        sBb.clear();

                        // unsafe cast ahoy!
                        // TODO: figure out how to make this not an unsafe cast
                        SocketChannel sc = (SocketChannel) s.channel();
                        if (sc.read(sBb) == -1) {
                            s.cancel();
                            mClientSockets.remove(sc);
                        } else System.out.println("client bytes:" + new String(sBb.array()));

                    }

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

}
