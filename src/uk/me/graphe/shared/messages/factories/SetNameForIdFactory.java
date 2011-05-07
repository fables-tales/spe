package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.SetNameForIdMessage;

public class SetNameForIdFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        try {
            int id = o.getInt("id");
            String message = o.getString("text");
            return new SetNameForIdMessage(id, message);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
