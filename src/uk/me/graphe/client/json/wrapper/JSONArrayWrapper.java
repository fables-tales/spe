package uk.me.graphe.client.json.wrapper;

import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class JSONArrayWrapper extends JSONArray {

    private JSONObject[] mBack;
    
    public JSONArrayWrapper(JSONObject[] javaScriptObjects) {
        mBack = javaScriptObjects;
    }
    

    @Override
    public JSONObject get(int i) throws JSONException {
        return mBack[i];
    }

    @Override
    public int length() {
        return mBack.length;
    }

}
