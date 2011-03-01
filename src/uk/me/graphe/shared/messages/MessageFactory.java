package uk.me.graphe.shared.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.factories.AddEdgeFactory;
import uk.me.graphe.shared.messages.factories.AddNodeFactory;
import uk.me.graphe.shared.messages.factories.ChatFactory;
import uk.me.graphe.shared.messages.factories.CompositeFactory;
import uk.me.graphe.shared.messages.factories.ConversionFactory;
import uk.me.graphe.shared.messages.factories.DeleteEdgeFactory;
import uk.me.graphe.shared.messages.factories.DeleteNodeFactory;
import uk.me.graphe.shared.messages.factories.HeartbeatFactory;
import uk.me.graphe.shared.messages.factories.MakeGraphFactory;
import uk.me.graphe.shared.messages.factories.MoveNodeFactory;
import uk.me.graphe.shared.messages.factories.NoSuchGraphFactory;
import uk.me.graphe.shared.messages.factories.OpenGraphFactory;
import uk.me.graphe.shared.messages.factories.RequestGraphFactory;
import uk.me.graphe.shared.messages.factories.StateIdFactory;



public abstract class MessageFactory {
    private static Map<String, ConversionFactory> sOpFactoryMap = null;

    static {
        // TODO: get the strings from the classes instead of hardcode them
        sOpFactoryMap = new HashMap<String, ConversionFactory>();
        sOpFactoryMap.put("addEdge", new AddEdgeFactory());
        sOpFactoryMap.put("delEdge", new DeleteEdgeFactory());
        sOpFactoryMap.put("addNode", new AddNodeFactory());
        sOpFactoryMap.put("delNode", new DeleteNodeFactory());
        sOpFactoryMap.put("movNode", new MoveNodeFactory());
        sOpFactoryMap.put("heartbeat", new HeartbeatFactory());
        sOpFactoryMap.put("makeGraph", new MakeGraphFactory());
        sOpFactoryMap.put("openGraph", new OpenGraphFactory());
        sOpFactoryMap.put("updateStateId", new StateIdFactory());
        sOpFactoryMap.put("requestGraph", new RequestGraphFactory());
        sOpFactoryMap.put("noSuchGraph", new NoSuchGraphFactory());
        sOpFactoryMap.put("composite", new CompositeFactory());
        sOpFactoryMap.put("chat", new ChatFactory());
    }

    public static List<Message> makeOperationsFromJson(List<JSONObject> jsos)
            throws JSONException {

        List<Message> result = new ArrayList<Message>();

        for (JSONObject o : jsos) {
            String messageVal = o.getString("message");
            Message op = MessageFactory.make(messageVal, o);
            result.add(op);
        }

        return result;
    }

    private static Message make(String messageVal, JSONObject o) {
        if (sOpFactoryMap.containsKey(messageVal)) {
            return sOpFactoryMap.get(messageVal).make(o);
        } else {
            throw new Error("message factory: got a message I couldn't decode");
        }
    }

}
