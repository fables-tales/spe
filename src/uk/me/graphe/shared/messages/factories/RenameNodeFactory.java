package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.operations.RenameNodeOperation;

public class RenameNodeFactory implements ConversionFactory {

    @Override
    public Message make(JSONObject o) {
        Vertex old;
        try {
            old = new Vertex(o.getString("oldNode"));
            String newN = o.getString("newNode");
            RenameNodeOperation rno = new RenameNodeOperation(old, newN);
            return rno;
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
