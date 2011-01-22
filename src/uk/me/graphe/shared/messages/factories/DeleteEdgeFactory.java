package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.operations.DeleteEdgeOperation;

public class DeleteEdgeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v1 = new Vertex(o.getString("from"));
            Vertex v2 = new Vertex(o.getString("to"));
            return new DeleteEdgeOperation(new Edge(v1, v2));
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
