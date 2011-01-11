package uk.me.graphe.server.messages.factories;

import org.json.JSONObject;

import uk.me.graphe.server.messages.HeartbeatMessage;
import uk.me.graphe.server.messages.Message;

public class HeartbeatFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        return new HeartbeatMessage();
    }

}
