package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.operations.AddEdgeOperation;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;

public class AddEdgeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v1 = new Vertex(o.getString("from"));
            Vertex v2 = new Vertex(o.getString("to"));
            return new AddEdgeOperation(new Edge(v1, v2));
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
