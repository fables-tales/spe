package uk.me.graphe.server.org.json.wrapper;

import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class JSONObjectWrapper extends JSONObject {

    @Override
    public String toString() {
        return mObject.toString();
    }

    private org.json.JSONObject mObject;

    public JSONObjectWrapper(String s) throws org.json.JSONException {
        mObject = new org.json.JSONObject(s);
    }

    public JSONObjectWrapper() {
        mObject = new org.json.JSONObject();
    }

    @Override
    public void append(String string, String json) throws JSONException {
        try {
            mObject.append(string, json);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

    @Override
    public int getInt(String string) throws JSONException {
        try {
            return mObject.getInt(string);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

    @Override
    public JSONArray getJSONArray(String string) throws JSONException {
        try {
            return new JSONArrayWrapper(mObject.getJSONArray(string));
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

    @Override
    public String getString(String key) throws JSONException {
        try {
            return mObject.getString(key);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

    @Override
    public boolean has(String string) {
        return mObject.has(string);

    }

    @Override
    public void put(String string, String string2) throws JSONException {
        try {
            mObject.put(string, string2);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

    @Override
    public void put(String string, int integer) throws JSONException {
        try {
            mObject.put(string, integer);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }

    }

}
