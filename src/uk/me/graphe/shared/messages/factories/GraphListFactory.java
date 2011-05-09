package uk.me.graphe.shared.messages.factories;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.GraphListMessage;

public class GraphListFactory implements ConversionFactory{

	 @Override
	    public Message make(JSONObject o) {

	        try {
	            String list = o.getString("list");

	            return new GraphListMessage(list);
	        } catch (JSONException e) {
	            throw new Error(e);
	        }
	    }
	
}
