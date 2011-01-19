package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.StateIdMessage;

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
