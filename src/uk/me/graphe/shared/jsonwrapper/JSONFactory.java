package uk.me.graphe.shared.jsonwrapper;

public interface JSONFactory {

    public JSONObject makeJSONObject(String s) throws JSONException;

    public JSONObject makeNullJSONObject();

}
