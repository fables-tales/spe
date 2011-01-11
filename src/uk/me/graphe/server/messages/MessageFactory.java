package uk.me.graphe.server.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.factories.AddEdgeFactory;
import uk.me.graphe.server.messages.factories.AddNodeFactory;
import uk.me.graphe.server.messages.factories.ConversionFactory;
import uk.me.graphe.server.messages.factories.DeleteEdgeFactory;
import uk.me.graphe.server.messages.factories.DeleteNodeFactory;
import uk.me.graphe.server.messages.factories.HeartbeatFactory;
import uk.me.graphe.server.messages.factories.MakeGraphFactory;
import uk.me.graphe.server.messages.factories.MoveNodeFactory;

public abstract class MessageFactory {
    private static Map<String, ConversionFactory> sOpFactoryMap = null;
    
    static {
        //TODO: get the strings from the classes instead of hardcode them
        sOpFactoryMap = new HashMap<String, ConversionFactory>();
        sOpFactoryMap.put("addEdge", new AddEdgeFactory());
        sOpFactoryMap.put("delEdge", new DeleteEdgeFactory());
        sOpFactoryMap.put("addNode", new AddNodeFactory());
        sOpFactoryMap.put("delNode", new DeleteNodeFactory());
        sOpFactoryMap.put("movNode", new MoveNodeFactory());
        sOpFactoryMap.put("heartbeat", new HeartbeatFactory());
        sOpFactoryMap.put("makeGraph", new MakeGraphFactory());
    }

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