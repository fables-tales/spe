package uk.me.graphe.shared.messages.factories;


import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.MakeGraphMessage;
import uk.me.graphe.shared.messages.Message;

public class MakeGraphFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        return new MakeGraphMessage();
    }

}
