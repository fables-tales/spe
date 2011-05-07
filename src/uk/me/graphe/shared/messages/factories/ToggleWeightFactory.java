package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.ToggleDrawWeightsMessage;

public class ToggleWeightFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        
        try {
            boolean result = o.getString("toggle").equals("yes");
            return new ToggleDrawWeightsMessage(result);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Error(e);
        }
    }

}
