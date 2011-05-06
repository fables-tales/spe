package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
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
            int weight = o.getInt("weight");
            System.err.println(dir);
            Edge e = new Edge(v1, v2, dir);
            e.setWeight(weight, 0);
            System.err.println("got weight:" + weight);
            System.err.println("e-weight:" + e.getWeight(0));
            return new AddEdgeOperation(e);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
