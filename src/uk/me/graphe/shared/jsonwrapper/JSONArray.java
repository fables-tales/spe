package uk.me.graphe.shared.jsonwrapper;

import uk.me.graphe.shared.jsonwrapper.JSONException;

public abstract class JSONArray {

    public abstract int length();

    public abstract Object get(int i) throws JSONException;
    

}
