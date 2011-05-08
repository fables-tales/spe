package uk.me.graphe.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public class ClientManager {
    private static ClientManager sIntance;
    private ConcurrentMap<SocketChannel, Client> mClientMap = new ConcurrentHashMap<SocketChannel, Client>();

    private Selector mReadableSocketsSelector;
    private Selector mWritableSocketsSelector;
    private CountDownLatch mSelectionBlock = new CountDownLatch(0);
    private Map<Integer, List<Client>> mClientGraphMap = new HashMap<Integer, List<Client>>();

    private ClientManager() {
        try {
            mReadableSocketsSelector = Selector.open();
            mWritableSocketsSelector = Selector.open();
        } catch (IOException e) {
            // why this throws an io exception I don't know, but it does
            throw new Error(e);
        }
    }

    public static ClientManager getInstance() {
        if (sIntance == null) sIntance = new ClientManager();
        return sIntance;
    }

    public void addClient(Client client) {
        try {
            System.out.println("adding client");
            this.blockSelection();
            client.getChannel().configureBlocking(false);
            mClientMap.put(client.getChannel(), client);
            mReadableSocketsSelector.wakeup();
            mWritableSocketsSelector.wakeup();
            client.getChannel().register(mReadableSocketsSelector,
                    SelectionKey.OP_READ);
            client.getChannel().register(mWritableSocketsSelector,
                    SelectionKey.OP_WRITE);
            this.unblockSelection();
            System.out.println("done adding client");
        } catch (ClosedChannelException e) {
            // more unknown io exceptions
            throw new Error(e);
        } catch (IOException ioe) {
            throw new Error(ioe);
        }

    }

    private void unblockSelection() {
       mSelectionBlock.countDown();
    }

    private void blockSelection() {
        mSelectionBlock = new CountDownLatch(1);
    }

    /**
     * waits for readable clients to occur and then returns them
     * 
     * @return a set of readable clients
     */
    public Set<Client> waitOnReadableClients() {
        System.err.println("selecting");
        try {
            this.waitForUnblockedSelection();
            System.err.println("block passed");
            mReadableSocketsSelector.select();
        } catch (IOException e) {
            throw new Error(e);
        }
        System.err.println("selection passed");
        Set<Client> s = new HashSet<Client>();

        for (SelectionKey sk : mReadableSocketsSelector.selectedKeys()) {
            Client c = mClientMap.get(sk.channel());
            s.add(c);
        }
        
        System.err.println(s.size());

        return s;
    }

    private void waitForUnblockedSelection() {
        try {
            mSelectionBlock.await();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    public void disconnect(Client c) {
        this.blockSelection();
        c.disconnect();
        mReadableSocketsSelector.wakeup();
        mWritableSocketsSelector.wakeup();
        c.getChannel().keyFor(mReadableSocketsSelector).cancel();
        c.getChannel().keyFor(mWritableSocketsSelector).cancel();

        try {
            c.getChannel().close();
        } catch (IOException e) {
            throw new Error(e);
        }

        mClientMap.remove(c.getChannel());
        this.unblockSelection();

    }

    public void wakeUp() {
        this.blockSelection();
        this.mReadableSocketsSelector.wakeup();
        this.mWritableSocketsSelector.wakeup();
        this.unblockSelection();
    }


    public int getNumberOfClients() {
        return this.mClientMap.size();
    }

    public void setClientGraph(Client c, int i) {
        if (!mClientGraphMap.containsKey(i)) mClientGraphMap.put(i, new ArrayList<Client>());
        mClientGraphMap.get(i).add(c);
    }
    
    public List<Client> clientsForGraph(int currentGraphId) {
        return new ArrayList<Client>(mClientGraphMap.get(currentGraphId));
    }

    public void removeClientGraph(int id, Client client) {
        if (mClientGraphMap.get(id) != null) mClientGraphMap.get(id).remove(client);
        
    }

}
