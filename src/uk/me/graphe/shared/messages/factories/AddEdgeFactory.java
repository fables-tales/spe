package uk.me.graphe.shared.messages.factories;


import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.operations.AddEdgeOperation;

public class AddEdgeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v1 = new Vertex(o.getString("from"));
            Vertex v2 = new Vertex(o.getString("to"));
            VertexDirection dir = VertexDirection.valueOf(o.getString("dir"));
            JSONArray a =  o.getJSONArray("weights");
            List<Integer> weights = new ArrayList<Integer>();
            for (int i = 0; i < a.length(); i++) {
                weights.add((Integer)a.get(i));
            }
            System.err.println(dir);
            Edge e = new Edge(v1, v2, dir);
            int weight = weights.get(0);
            e.setWeight(weight, 0);
            
            for (int i = 1; i < weights.size(); i++) e.addMultipleEdge(weights.get(i));
            return new AddEdgeOperation(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
