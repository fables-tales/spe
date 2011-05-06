package uk.me.graphe.client.json.wrapper;



import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.shared.jsonwrapper.JSONArray;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

import com.google.gwt.core.client.JavaScriptObject;

public class JSONObjectWrapper extends JSONObject {

    @Override
    public native String toString() /*-{
        return JSON.stringify(this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj);
    }-*/;

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
        if (this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] == null || typeof this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] == "undefined") this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] = [];
        this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string].push(json);
    }-*/;

    @Override
    public native int getInt(String string) throws JSONException /*-{
        return this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string];
    }-*/;
    
    private native String[] getArray(String string) /*-{
        var member = this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string];
        //var result = []
        //for (var i = 0; i < member.length; i++) {
        //    alert("faces")
        //    alert(member[i])
        //    result.push(JSON.stringify(member[i]));
        //}
        return member;
    }-*/;
    
    @Override
    public JSONArray getJSONArray(String string) throws JSONException {
        List<JSONObject> thing = new ArrayList<JSONObject>();
        for (String s : this.getArray(string)) {
            thing.add(new JSONObjectWrapper(s));
        }
        
        return new JSONArrayWrapper(thing.toArray(new JSONObjectWrapper[]{new JSONObjectWrapper("{}")}));
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

    @Override
    public native void put(String string, int[] things) throws JSONException /*-{
        this.@uk.me.graphe.client.json.wrapper.JSONObjectWrapper::mObj[string] = things;
    }-*/;

}
