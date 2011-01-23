package uk.me.graphe.server.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import uk.me.graphe.server.database.dbitems.*;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2dImpl;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.EdgeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;
import uk.me.graphe.shared.messages.operations.MoveNodeOperation;
import uk.me.graphe.shared.messages.operations.NodeOperation;

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
        // TODO Auto-generated method stub
        
    }

    @Override
    public OTGraphManager2d retrieve(int key) {
        List<OTGraphManager2dStore> retrieves = mData.find(OTGraphManager2dStore.class, "id=", key).asList();
        if (retrieves.size() != 1)
            throw new Error("Could not locate item");
        OTGraphManager2dStore retrieve = retrieves.get(0);
        OTGraphManager2d toReturn = new OTGraphManager2dImpl(retrieve.getId());
        toReturn.setStateId(retrieve.getStateid());
        return toReturn;
    }

    @Override
    public int store(OTGraphManager2d manager) {
        OTGraphManager2dStore toStore = new OTGraphManager2dStore(manager);
        List<GraphDB> storedOperations = new ArrayList<GraphDB>();
        CompositeOperation history = manager.getCompleteHistory();
        List<GraphOperation> operations = history.asIndividualOperations();
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
        toStore.setmOps(storedOperations);
        mData.save(toStore);
        return toStore.getId();
    }

    

}
