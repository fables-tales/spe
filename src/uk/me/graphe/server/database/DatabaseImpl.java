package uk.me.graphe.server.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import uk.me.graphe.server.database.dbitems.*;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2dImpl;
import uk.me.graphe.shared.graphmanagers.OTGraphManagerFactory;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.MessageFactory;
import uk.me.graphe.shared.messages.operations.AddEdgeOperation;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.DeleteEdgeOperation;
import uk.me.graphe.shared.messages.operations.DeleteNodeOperation;
import uk.me.graphe.shared.messages.operations.EdgeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;
import uk.me.graphe.shared.messages.operations.MoveNodeOperation;
import uk.me.graphe.shared.messages.operations.NoOperation;
import uk.me.graphe.shared.messages.operations.NodeOperation;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class DatabaseImpl implements Database{
    
    private Mongo mMongo;
    private Morphia mMorphia = new Morphia();
    private Datastore mData;
    DBCollection mCollection;
    
    public DatabaseImpl () {
        try {
            mMongo = new Mongo();
        } catch (UnknownHostException e) {
            return;
        }
        mData = mMorphia.createDatastore(mMongo, "graphs");
        mCollection = mMongo.getDB("graphs").getCollection("OTGraphManager2dStore");
    }

    @Override
    public void delete(int key) {
        mData.delete(mData.createQuery(OTGraphManager2dStore.class).filter("id =", key));
    }
    
    @Override
    public void clean() {
        mCollection.drop();
    }

    @Override
    public int size() {
        return (int) mCollection.getCount();
    }
    
    @Override
    public OTGraphManager2d retrieve(int key) {
        //  Extract OtGraphManagerStore from DB
        List<OTGraphManager2dStore> retrieves = mData.find(OTGraphManager2dStore.class, "id =", key).asList();
        if (retrieves == null || retrieves.size() != 1)
            return null;
        OTGraphManager2dStore retrieve = retrieves.get(0);
        List<GraphOperation> operations = new ArrayList<GraphOperation>();
        List<JSONObject> objects = new LinkedList<JSONObject>();
        List<Message> messages = null;
        for (String s : retrieve.getmOps()) {
            JSONObject item = parseItem(s);
            objects.add(item);
        }
        try {
            messages = MessageFactory.makeOperationsFromJson(objects);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int i = 0;
        for (Message item : messages) {
            // Store all operations as local, map to server in restoreState()
            operations.add((GraphOperation) item);
            i++;
        }
        OTGraphManager2d toReturn = OTGraphManagerFactory.newInstance(key);
        toReturn.setHistory(operations);
        return toReturn;
    }
    
    private JSONObject parseItem (String json) {
        JSONObject object = null;
        try {
            object = JSONImplHolder.make(json) ;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }
    

    @Override
    public int store(OTGraphManager2d manager) {
        
        OTGraphManager2dStore toStore = new OTGraphManager2dStore(manager);
        CompositeOperation history;
        // Check whether the graph exists in the database already
        List<OTGraphManager2dStore> retrieves = null;
        Query<OTGraphManager2dStore> exists =  mData.find(OTGraphManager2dStore.class, "id =", manager.getGraphId());
        retrieves = exists.asList();
        if (retrieves == null || retrieves.isEmpty()) {
            history = manager.getCompleteHistory();
            return newgraph(convertOperations(history), toStore);   
        }
        else if (retrieves.size() > 1)
            throw new Error("Duplicate items");
        else if (retrieves.size() == 1) {
            // Check state id of update is greater than value in database
            toStore = retrieves.get(0);
            System.out.println(toStore);
            System.out.println(toStore.getId());
            if (manager.getStateId() <= toStore.getStateid())
                return manager.getGraphId();
            history = manager.getOperationDelta(toStore.getStateid());
            return updategraph(convertOperations(history), manager.getGraphId(), manager.getStateId());
        }
        else {
            history = manager.getCompleteHistory();
            return newgraph(convertOperations(history), toStore);
        }
    }
    
    private int newgraph (List<String> operations, OTGraphManager2dStore toStore) {
        toStore.setmOps(operations);
        mData.save(toStore);
        return toStore.getId();
    }
    
    private int updategraph (List<String> operations, int id, int newid) {
        Query<OTGraphManager2dStore> updateQuery = mData.createQuery(OTGraphManager2dStore.class).filter("id =", id);
        UpdateOperations<OTGraphManager2dStore> ops = 
            mData.createUpdateOperations(OTGraphManager2dStore.class).add("mOps", operations, true).set("stateid", newid);
        mData.update(updateQuery, ops);
        return id;
    }
    private List<String> convertOperations(CompositeOperation history) {
        List<GraphOperation> operations = history.asIndividualOperations();
        List<String> storedOperations = new ArrayList<String>();
        for (GraphOperation op : operations) {
            storedOperations.add(op.toJson());
        }
        return storedOperations;
    }
}
