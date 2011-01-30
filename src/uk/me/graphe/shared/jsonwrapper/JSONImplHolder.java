package uk.me.graphe.shared.jsonwrapper;


public class JSONImplHolder {
    private static JSONFactory sJSONFactory;

    public static JSONObject make(String s) throws JSONException {
        return sJSONFactory.makeJSONObject(s);
    }

    public static JSONObject make() {
        return sJSONFactory.makeNullJSONObject();
    }

    public static void initialise(JSONFactory jsonWrapperFactory) {
        if (sJSONFactory == null) sJSONFactory = jsonWrapperFactory;
        else throw new Error("json already initialised");
    }
}
