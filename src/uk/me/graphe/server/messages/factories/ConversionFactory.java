package uk.me.graphe.server.messages.factories;

import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;

public interface ConversionFactory {

    public Message make(JSONObject o);
}
