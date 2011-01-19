package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.OpenGraphMessage;

public class OpenGraphFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        int id;
        try {
            id = o.getInt("id");
            return new OpenGraphMessage(id);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
