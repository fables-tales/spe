package uk.me.graphe.server.messages;

import org.json.JSONObject;

public interface ConversionFactory {

    public Message make(JSONObject o);
}
