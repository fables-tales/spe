package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.operations.MoveNodeOperation;
import uk.me.graphe.shared.Vertex;

public class MoveNodeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v = new Vertex(o.getString("name"));
            int x = o.getInt("tox");
            int y = o.getInt("toy");
            return new MoveNodeOperation(v, x, y);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
    