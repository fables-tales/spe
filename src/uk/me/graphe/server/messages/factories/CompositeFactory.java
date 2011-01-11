package uk.me.graphe.server.messages.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.MessageFactory;
import uk.me.graphe.server.messages.operations.CompositeOperation;
import uk.me.graphe.server.messages.operations.GraphOperation;

public class CompositeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        // ASDFHASHGJSADHFJASDFGHJASDFHJASGHJASDFHJASHDGJASHDFJSH

        List<JSONObject> messages = new ArrayList<JSONObject>();

        JSONArray values;
        try {
            values = o.getJSONArray("values");
            System.err.println(values.length());
            for (int i = 0; i < values.length(); i++) {
                String s = values.get(i).toString();
                messages.add(new JSONObject(s));
            }
            
            List<Message> objs = MessageFactory.makeOperationsFromJson(messages);
            List<GraphOperation> op = new ArrayList<GraphOperation>();
            for (Message m : objs) {
                op.add((GraphOperation)m);
            }
            
            return new CompositeOperation(op);
            
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
