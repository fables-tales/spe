package uk.me.graphe.server.messages.factories;

import org.json.JSONObject;

import uk.me.graphe.server.messages.MakeGraphMessage;
import uk.me.graphe.server.messages.Message;

public class MakeGraphFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        return new MakeGraphMessage();
    }

}
