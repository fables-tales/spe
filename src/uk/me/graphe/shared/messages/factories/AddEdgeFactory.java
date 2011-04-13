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
            System.err.println(dir);
            return new AddEdgeOperation(new Edge(v1, v2, dir));
        } catch (JSONException e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
