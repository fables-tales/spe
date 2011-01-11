package uk.me.graphe.server.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class MakeGraphMessage extends Message {

    @Override
    public String getMessage() {
        return "makeGraph";
    }

    @Override
    public String toJson() {
        JSONObject jso = new JSONObject();
        try {
            jso.put("message", this.getMessage());
            return jso.toString();
        } catch (JSONException jse) {
            throw new Error(jse);
        }
    }

}
