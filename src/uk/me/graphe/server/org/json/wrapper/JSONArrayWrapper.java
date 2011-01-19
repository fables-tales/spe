package uk.me.graphe.server.org.json.wrapper;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONArray;


public class JSONArrayWrapper extends JSONArray {

    private org.json.JSONArray mBack;
    
    public JSONArrayWrapper(org.json.JSONArray jsonArray) {
        mBack = jsonArray;
    }


    @Override
    public int length() {
        return mBack.length();
    }


    @Override
    public Object get(int i) throws JSONException {
        try {
            return mBack.get(i);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

}
