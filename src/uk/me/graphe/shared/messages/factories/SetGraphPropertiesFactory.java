package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.SetGraphPropertiesMessage;

public class SetGraphPropertiesFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        
        try {
            boolean w = o.getString("weight").equals("yes");
            boolean d = o.getString("dir").equals("yes");
            boolean f = o.getString("flow").equals("yes");
            return new SetGraphPropertiesMessage(w, d, f);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
