package uk.me.graphe.server.database;

import uk.me.graphe.shared.Graph;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class DatabaseImpl implements Database{
    
    private Datastore mData;
    
    public DatabaseImpl () {
        
    }

    @Override
    public void delete(int key) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Graph retrieve(int key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int store(Graph graph) {
        // TODO Auto-generated method stub
        return 0;
    }

}
