package uk.me.graphe.server;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientManager {
    private static ClientManager sIntance;
    private ConcurrentMap<SocketChannel, Client> mClientMap = new ConcurrentHashMap<SocketChannel, Client>();

    private Selector mReadableSocketsSelector;
    private Selector mWritableSocketsSelector;

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
        mClientMap.put(client.getChannel(), client);

        try {
            mReadableSocketsSelector.wakeup();
            mWritableSocketsSelector.wakeup();
            client.getChannel().register(mReadableSocketsSelector, SelectionKey.OP_READ);
            client.getChannel().register(mReadableSocketsSelector, SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            // more unknown io exceptions
            throw new Error(e);
        }

    }
    
    /**
     * waits for readable clients to occur and then returns them
     * @return a set of readable clients
     */
    public Set<Client> waitOnReadableClients() {
        try {
            mReadableSocketsSelector.select();
        } catch (IOException e) {
            throw new Error(e);
        }

        Set<Client> s = new HashSet<Client>();

        for (SelectionKey sk : mReadableSocketsSelector.selectedKeys()) {
            Client c = mClientMap.get(sk.channel());
            s.add(c);
        }

        return s;
    }
    
    public void disconnect(Client c) {
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
    }

}
