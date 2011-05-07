package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class ToggleDrawWeightsMessage extends Message {

    private boolean mResult;

    public ToggleDrawWeightsMessage(boolean x) {
        mResult = x;
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("toggle", mResult ? "yes" : "no");
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

    @Override
    public String getMessage() {
        return "tdwi";
    }

}
