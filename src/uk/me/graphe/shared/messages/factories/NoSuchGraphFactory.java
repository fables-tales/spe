package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.NoSuchGraphMessage;

public class NoSuchGraphFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        return new NoSuchGraphMessage();
    }

}
