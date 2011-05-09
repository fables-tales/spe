package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.AddPrivsMessage;
import uk.me.graphe.shared.messages.Message;

public class AddPrivsFactory implements ConversionFactory{
    @Override
    public Message make(JSONObject o) {

        try {
            String emailAddress = o.getString("emailAddress");
            String graphId = o.getString("graphId");
            return new AddPrivsMessage(emailAddress, graphId);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }
}
