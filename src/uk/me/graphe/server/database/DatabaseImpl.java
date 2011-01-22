package uk.me.graphe.server.database;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;


import uk.me.graphe.server.database.dbitems.GraphDB;
import uk.me.graphe.server.database.dbitems.OTGraphManager2dStore;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2dImpl;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.EdgeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;
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
        CompositeOperation history = manager.getCompleteHistory();
        List<GraphOperation> operations = history.asIndividualOperations();
        for (GraphOperation item : operations) {
            if (item.isEdgeOperation()) {
                EdgeOperation edgeOp = item.asEdgeOperation();
                Edge edge = edgeOp.getEdge();
                if (edgeOp.createsEdge(edge))
                    // Create DBEAddEdge
                if (edgeOp.deletesEdge(edge))
                    // Create DBDeleteEdge
                    ;
            }
            if (item.isNodeOperation()) {
                NodeOperation nodeOp = item.asNodeOperation();
                Vertex vertex = nodeOp.getNode();
                if (nodeOp.createsNode(vertex))
                    
                if(nodeOp.deletesNode(vertex))
                    
                if(nodeOp.movesNode(vertex))
                    ;
            }
            if (item.isNoOperation()) {
                
            }
        }
        mData.save(toStore);
        return toStore.getId();
    }

    

}
