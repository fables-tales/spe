package uk.me.graphe.server.org.json.wrapper;



import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONFactory;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class JSONWrapperFactory implements JSONFactory {

    @Override
    public JSONObject makeJSONObject(String s) throws JSONException {
        try {
            return new JSONObjectWrapper(s);
        } catch (org.json.JSONException e) {
            throw new JSONException(e);
        }
    }

    @Override
    public JSONObject makeNullJSONObject() {
        return new JSONObjectWrapper();
    }

}
