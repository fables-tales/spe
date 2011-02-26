package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.ChatMessage;
import uk.me.graphe.shared.messages.Message;

public class ChatFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {

        try {
            String id = o.getString("id");
            String text = o.getString("text");
            return new ChatMessage(id, text);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}