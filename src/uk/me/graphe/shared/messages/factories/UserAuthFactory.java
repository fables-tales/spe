package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.UserAuthMessage;
import uk.me.graphe.shared.messages.Message;

public class UserAuthFactory implements ConversionFactory{
    @Override
    public Message make(JSONObject o) {

        try {
            String opUrl = o.getString("opUrl");
            String reUrl = o.getString("reUrl");
            String emailAdd = o.getString("emailAdd");
            String openIdUrl = o.getString("openIdUrl");
            String authKey = o.getString("authKey");
            return new UserAuthMessage(opUrl, reUrl, emailAdd, openIdUrl, authKey);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }
}
