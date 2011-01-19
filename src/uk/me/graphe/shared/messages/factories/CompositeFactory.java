package uk.me.graphe.shared.messages.factories;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.MessageFactory;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;




public class CompositeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        // ASDFHASHGJSADHFJASDFGHJASDFHJASGHJASDFHJASHDGJASHDFJSH

        List<JSONObject> messages = new ArrayList<JSONObject>();

        JSONArray values;
        try {
            List<GraphOperation> op = new ArrayList<GraphOperation>();
            if (o.has("values")) {
                values = o.getJSONArray("values");
                System.err.println(values.length());
                for (int i = 0; i < values.length(); i++) {
                    String s = values.get(i).toString();
                    messages.add(JSONImplHolder.make(s));
                }

                List<Message> objs = MessageFactory
                        .makeOperationsFromJson(messages);

                for (Message m : objs) {
                    op.add((GraphOperation) m);
                }
            }

            return new CompositeOperation(op);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
