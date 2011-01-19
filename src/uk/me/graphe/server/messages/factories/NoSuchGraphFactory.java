package uk.me.graphe.server.messages.factories;

import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.NoSuchGraphMessage;

public class NoSuchGraphFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        return new NoSuchGraphMessage();
    }

}
