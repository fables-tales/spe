package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.operations.DeleteNodeOperation;
import uk.me.graphe.shared.Vertex;

public class DeleteNodeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v = new Vertex(o.getString("name"));
            return new DeleteNodeOperation(v);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
