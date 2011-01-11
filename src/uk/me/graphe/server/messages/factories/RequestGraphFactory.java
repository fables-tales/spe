package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.RequestGraphMessage;

public class RequestGraphFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {

        try {
            int id = o.getInt("id");
            int since = o.getInt("since");
            return new RequestGraphMessage(id, since);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
