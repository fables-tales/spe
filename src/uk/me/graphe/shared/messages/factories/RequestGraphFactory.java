package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.RequestGraphMessage;

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
