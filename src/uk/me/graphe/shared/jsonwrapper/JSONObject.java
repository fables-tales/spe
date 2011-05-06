package uk.me.graphe.shared.jsonwrapper;

public abstract class JSONObject {
    
    public abstract boolean has(String string);

    public abstract JSONArray getJSONArray(String string) throws JSONException;

    public abstract String getString(String key) throws JSONException;

    public abstract void put(String string, String string2) throws JSONException;

    public abstract void put(String string, int mId) throws JSONException;
    
    public abstract void put(String string, int[] things) throws JSONException;

    public abstract int getInt(String string) throws JSONException;

    public abstract void append(String string, String json) throws JSONException;

}
