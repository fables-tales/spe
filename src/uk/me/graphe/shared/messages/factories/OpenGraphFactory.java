package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.OpenGraphMessage;

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
