package uk.me.graphe.server.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class MessageFactory {
    private static final Map<String, ConversionFactory> sOpFactoryMap = null;
    

	public static List<Message> makeOperationsFromJson(List<JSONObject> jsos) throws JSONException {
		
		List<Message> result = new ArrayList<Message>();
		
		for (JSONObject o : jsos) {
			String messageVal = o.getString("message");
			Message op = MessageFactory.make(messageVal, o);
			result.add(op);
		}
		
		return result;
	}

	private static Message make(String messageVal, JSONObject o) {
		return sOpFactoryMap.get(messageVal).make(o);
	}
	
	
}
