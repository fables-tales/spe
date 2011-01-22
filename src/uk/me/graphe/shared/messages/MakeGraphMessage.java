package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class MakeGraphMessage extends Message {

    @Override
    public String getMessage() {
        return "makeGraph";
    }

    @Override
    public String toJson() {
        JSONObject jso = JSONImplHolder.make();
        try {
            jso.put("message", this.getMessage());
            return jso.toString();
        } catch (JSONException jse) {
            throw new Error(jse);
        }
    }

}
