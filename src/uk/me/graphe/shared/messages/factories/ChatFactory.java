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
            boolean notify = Boolean.valueOf(o.getString("notify"));
            boolean writing = Boolean.valueOf(o.getString("writing"));
            return new ChatMessage(id, text, notify, writing);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}