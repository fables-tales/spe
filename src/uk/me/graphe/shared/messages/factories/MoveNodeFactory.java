package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.operations.MoveNodeOperation;

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
    