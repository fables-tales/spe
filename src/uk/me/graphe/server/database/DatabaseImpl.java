package uk.me.graphe.server.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


import uk.me.graphe.server.database.dbitems.*;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2dImpl;
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
import com.mongodb.DB;
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
        if (retrieves.size() != 1)
            return null;
        OTGraphManager2dStore retrieve = retrieves.get(0);
        List<GraphDB> operations = retrieve.getmOps();
        // Create new 2d Graph manager
        OTGraphManager2d toReturn = new OTGraphManager2dImpl(retrieve.getId());
        toReturn.setStateId(retrieve.getStateid());
        List<GraphOperation> graphOperations = new ArrayList<GraphOperation>();
        
        for (GraphDB item : operations) {
            GraphOperation toAdd = null;
            int itemId = item.getHistoryId();
            if (item.isEdgeOperation()) {
                EdgeDB edgeOp = item.asEdgeOperation();
                DBEdge edge = edgeOp.getEdge();
                Edge thisEdge = new Edge(new Vertex(edge.getFrom().getLabel()), new Vertex(edge.getTo().getLabel()), edge.getDir());
                if (edgeOp.createsEdge(edge)) {
                    toAdd = new AddEdgeOperation(thisEdge);
                    }
                if (edgeOp.deletesEdge(edge)) {
                    toAdd = new DeleteEdgeOperation(thisEdge);
                }
            }
            if (item.isNodeOperation()) {
                NodeDB nodeOp = item.asNodeOperation();
                DBVertex vertex = nodeOp.getNode();
                Vertex thisVertex = new Vertex(vertex.getLabel());
                if (nodeOp.createsNode(vertex)) {
                    AddNodeDB addOp = (AddNodeDB) nodeOp;
                    toAdd = new AddNodeOperation(thisVertex, addOp.getX(), addOp.getY());
                }        
                if(nodeOp.deletesNode(vertex)) {
                    toAdd = new DeleteNodeOperation(thisVertex);
                }
                    
                if(nodeOp.movesNode(vertex)) {
                    MoveNodeDB moveOp = (MoveNodeDB) nodeOp;
                    toAdd = new MoveNodeOperation(thisVertex, moveOp.getToX(), moveOp.getToY());
                }
            }
            if (item.isNoOperation()) {
                toAdd = new NoOperation();
            }
            toAdd.setHistoryId(itemId);
            graphOperations.add(toAdd);
        }
        toReturn.setHistory(graphOperations);
        return toReturn;
    }

    @Override
    public int store(OTGraphManager2d manager) {
        
        OTGraphManager2dStore toStore = new OTGraphManager2dStore(manager);
        CompositeOperation history;
        // Check whether the graph exists in the database already
        List<OTGraphManager2dStore> retrieves = mData.find(OTGraphManager2dStore.class, "id =", manager.getGraphId()).asList();
        if (retrieves.size() > 1)
            throw new Error("Duplicate items");
        else if (retrieves.size() == 1) {
            // Check state id of update is greater than value in database
            toStore = retrieves.get(0);
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
    
    private int newgraph (List<GraphDB> operations, OTGraphManager2dStore toStore) {
        toStore.setmOps(operations);
        mData.save(toStore);
        return toStore.getId();
    }
    
    private int updategraph (List<GraphDB> operations, int id, int newid) {
        Query<OTGraphManager2dStore> updateQuery = mData.createQuery(OTGraphManager2dStore.class).filter("id =", id);
        UpdateOperations<OTGraphManager2dStore> ops = 
            mData.createUpdateOperations(OTGraphManager2dStore.class).add("mOps", operations, true).set("stateid", newid);
        mData.update(updateQuery, ops);
        return id;
    }
    private List<GraphDB> convertOperations(CompositeOperation history) {
        List<GraphOperation> operations = history.asIndividualOperations();
        List<GraphDB> storedOperations = new ArrayList<GraphDB>();
        for (GraphOperation item : operations) {
            GraphDB toAdd = null;
            int itemId = item.getHistoryId();
            if (item.isEdgeOperation()) {
                EdgeOperation edgeOp = item.asEdgeOperation();
                Edge edge = edgeOp.getEdge();
                DBEdge storeEdge = new DBEdge(edge, new DBVertex(edge.getFromVertex()), new DBVertex(edge.getToVertex()));
                if (edgeOp.createsEdge(edge)) {
                    toAdd = new AddEdgeDB(storeEdge);
                    }
                if (edgeOp.deletesEdge(edge)) {
                    toAdd = new DeleteEdgeDB(storeEdge);
                }
            }
            if (item.isNodeOperation()) {
                NodeOperation nodeOp = item.asNodeOperation();
                Vertex vertex = nodeOp.getNode();
                DBVertex storeVertex = new DBVertex(vertex);
                if (nodeOp.createsNode(vertex)) {
                    AddNodeOperation addOp = (AddNodeOperation) nodeOp;
                    toAdd = new AddNodeDB(storeVertex, addOp.getX(), addOp.getY());
                }        
                if(nodeOp.deletesNode(vertex)) {
                    toAdd = new DeleteNodeDB(storeVertex);
                }
                    
                if(nodeOp.movesNode(vertex)) {
                    MoveNodeOperation moveOp = (MoveNodeOperation) nodeOp;
                    toAdd = new MoveNodeDB(storeVertex, moveOp.getToX(), moveOp.getToY());
                }
            }
            if (item.isNoOperation()) {
                toAdd = new NoOpDB();
            }
            toAdd.setHistoryId(itemId);
            storedOperations.add(toAdd);
        }
        return storedOperations;
    }
}
