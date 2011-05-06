package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class SetNameForIdMessage extends Message {
    
    private int mGraphId;
    private String mNewName;
    
    public SetNameForIdMessage(int id, String newName) {
        mGraphId = id;
        mNewName = newName;
    }
    
    
    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("id", mGraphId);
            repr.put("text", mNewName);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

    @Override
    public String getMessage() {
        return "setNameForId";
    }


    public int getId() {
        return mGraphId;
    }


    public String getTitle() {
        return mNewName;
    }

}
