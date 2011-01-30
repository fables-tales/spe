package uk.me.graphe.client.json.wrapper;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONFactory;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class JSOFactory implements JSONFactory {

    @Override
    public JSONObject makeJSONObject(String s) throws JSONException {
        return new JSONObjectWrapper(s);
    }

    @Override
    public JSONObject makeNullJSONObject() {
        return new JSONObjectWrapper("{}");
    }

}
