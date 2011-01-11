package uk.me.graphe.server.messages;

import org.json.JSONException;
import org.json.JSONObject;

public class OpenGraphMessage extends Message {

    private int mId;

    public OpenGraphMessage(int id) {
        mId = id;
    }

    @Override
    public String getMessage() {
        return "opengraph";
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            repr.put("message", getMessage());
            repr.put("id", mId);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

}
