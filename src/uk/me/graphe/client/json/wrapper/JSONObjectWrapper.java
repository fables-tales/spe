package uk.me.graphe.client.json.wrapper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class JSONObjectWrapper extends JSONObject {

    private JavaScriptObject mObj;

    public JSONObjectWrapper(String s) {
        mObj = this.createObject(s);
    }

    private native JavaScriptObject createObject(String s) /*-{
        var obj = eval("(" + s + ")");
        return obj;
    }-*/;

    @Override
    public native void append(String string, String json) throws JSONException /*-{
        this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string].push(json);
    }-*/;

    @Override
    public native int getInt(String string) throws JSONException /*-{
        return this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string];
    }-*/;
    
    private native JsArray<JavaScriptObject> getArray(String string) /*-{
        var member = this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string];
        return member;
    }-*/;
    
    @Override
    public JSONArray getJSONArray(String string) throws JSONException {
        return new JSONArrayWrapper(this.getArray(string));
    }


    @Override
    public native String getString(String key) throws JSONException /*-{
        return this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[key];
    }-*/;

    @Override
    public native boolean has(String string) /*-{
        return this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] != null;
    }-*/;
    

    @Override
    public native void put(String string, String string2) throws JSONException /*-{
        this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] = string2;
    }-*/;

    @Override
    public native void put(String string, int mId) throws JSONException /*-{
        this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] = mId;
    }-*/;

}
