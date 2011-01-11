package uk.me.graphe.server.messages.factories;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.StateIdMessage;

public class StateIdFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        int id;
        try {
            id = o.getInt("graph");
            int state = o.getInt("state");
            return new StateIdMessage(id, state);
        } catch (JSONException e) {
            throw new Error(e);
        }

    }

}
