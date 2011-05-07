package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.operations.AddNodeOperation;
import uk.me.graphe.shared.messages.operations.SetStyleOperation;

public class SetStyleFactory implements ConversionFactory{

    @Override
    public Message make(JSONObject o) {
        try {
            Vertex v = new Vertex(o.getString("name"));
            int styleCode = o.getInt("style");
            return new SetStyleOperation(v, styleCode);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }

}
