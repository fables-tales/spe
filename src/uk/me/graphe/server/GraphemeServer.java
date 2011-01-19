package uk.me.graphe.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import uk.me.graphe.server.org.json.wrapper.JSONWrapperFactory;
import uk.me.graphe.server.ot.GraphProcessor;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;

public class GraphemeServer extends Thread {

    public static final int GAPHEME_PORT = 6689;
    private static GraphemeServer sInstance = null;

    public static GraphemeServer getInstance() {
        if (sInstance == null) sInstance = new GraphemeServer();
        return sInstance;
    }

    private ClientMessageHandler mClientMessageHandler;

    private ClientMessageSender mClientMessageSender;

    private boolean mRunning = false;

    private ServerSocketChannel mServerSocketChannel;

    private ClientManager mClientManager = ClientManager.getInstance();

    private GraphemeServer() {
        try {
            // sets up a server socket listening on the grapheme port
            JSONImplHolder.initialise(new JSONWrapperFactory());
            mServerSocketChannel = ServerSocketChannel.open();
            mServerSocketChannel.socket().bind(
                    new InetSocketAddress(GAPHEME_PORT));
            // let's make sure for certain we're up and running
            assert mServerSocketChannel.isOpen();

            // start a new client message handler: it's going to accept incoming
            // data from clients
            mClientMessageHandler = ClientMessageHandler.getInstance();
            mClientMessageSender = ClientMessageSender.getInstance();
            GraphProcessor.getInstance().start();

            mClientMessageHandler.start();
            mClientMessageSender.start();
        } catch (IOException e) {
            throw new Error("couldn't start server", e);
        }

    }

    public boolean isShutDown() {
        return !mRunning;
    }

    @Override
    public void run() {
        while (mRunning) {
            try {
                // accept incoming connections and let the client message
                // handler know about them
                SocketChannel clientSock = mServerSocketChannel.accept();
                Client client = new Client(clientSock);
                mClientManager.addClient(client);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void shutDown() {
        mRunning = false;
        mClientMessageHandler.shutDown();
        mClientMessageSender.shutDown();
        GraphProcessor.getInstance().shutDown();
        this.interrupt();
    }

    @Override
    public synchronized void start() {
        if (!this.mRunning) {
            mRunning = true;
            super.start();
        }
    }

    public void waitTornDown() {
        while (mClientMessageHandler.isAlive()
                || mClientMessageSender.isAlive()
                || GraphProcessor.getInstance().isAlive() || this.isAlive()) {
            System.err.println("cmh: " + mClientMessageHandler.isAlive());
            System.err.println("cms: " + mClientMessageSender.isAlive());
            System.err.println("gp: " + GraphProcessor.getInstance().isAlive());
            System.err.println("this: " + this.isAlive());

        }

    }

}
