package uk.me.graphe.client.json.wrapper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;

public class JSONArrayWrapper extends JSONArray {

    private JsArray<JavaScriptObject> mBack;
    
    public JSONArrayWrapper(JsArray<JavaScriptObject> array) {
        mBack = array;
    }
    

    @Override
    public Object get(int i) throws JSONException {
        return mBack.get(i);
    }

    @Override
    public int length() {
        return mBack.length();
    }

}
