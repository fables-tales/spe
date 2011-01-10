package uk.me.graphe.server.database;

import java.net.UnknownHostException;

import uk.me.graphe.shared.Graph;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;

public class DatabaseImpl implements Database{
    
    private Mongo mMongo;
    private Morphia mMorphia = new Morphia();
    private Datastore mData;
    
    public DatabaseImpl () {
        try {
            mMongo = new Mongo();
        } catch (UnknownHostException e) {
            return;
        }
        mData = mMorphia.createDatastore(mMongo, "graphs");
    }

    @Override
    public void delete(int key) {
        mData.delete(DBItem.class, key);
    }

    @Override
    public Graph retrieve(int key) {
        DBItem toRetrieve = mData.get(DBItem.class, key);
        return toRetrieve.getGraph();
    }

    @Override
    public int store(Graph graph) {
        DBItem toStore = new DBItem(graph);
        mData.save(toStore);
        return toStore.getID();
    }

}
