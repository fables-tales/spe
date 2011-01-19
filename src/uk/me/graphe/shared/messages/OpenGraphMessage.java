package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class OpenGraphMessage extends Message {

    private int mId;

    public OpenGraphMessage(int id) {
        mId = id;
    }

    @Override
    public String getMessage() {
        return "openGraph";
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("id", mId);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

    public int getId() {
        return mId;
    }

}
