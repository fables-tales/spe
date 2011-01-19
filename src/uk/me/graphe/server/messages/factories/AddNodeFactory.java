package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.Vertex;

public class AddNodeFactory implements ConversionFactory {


    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v = new Vertex(o.getString("name"));
            int x = o.getInt("x");
            int y = o.getInt("y");
            return new AddNodeOperation(v, x, y);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }
}
