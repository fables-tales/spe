package uk.me.graphe.server.database;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.me.graphe.server.database.dbitems.DBVertex;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;

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
        //mData.delete(DBItem.class, key);
    }

    @Override
    public Graph retrieve(int key) {
        //DBItem toRetrieve = mData.get(DBItem.class, key);
        return null;
    }

    @Override
    public int store(Graph graph) {
        List<Edge> edges = graph.getEdges();
        List<Vertex> vertices = graph.getVertices();
        Map<Vertex, DBVertex> vertexMap = storeVertices(vertices);
        storeEdges(edges);
    }

    private void storeEdges(List<Edge> edges) {
        // TODO Auto-generated method stub
        
    }

    private Map<Vertex, DBVertex> storeVertices(List<Vertex> vertices) {
        Map<Vertex, DBVertex> map = new HashMap<Vertex, DBVertex>();
        for(Vertex item: vertices) {
            DBVertex newvertex = new DBVertex(item);
            map.put(item, newvertex);
            mData.save(newvertex);
        }
        return map;
    }
    

}
