package uk.me.graphe.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.MessageFactory;
import uk.me.graphe.server.messages.OpenGraphMessage;
import uk.me.graphe.server.messages.StateIdMessage;
import uk.me.graphe.server.messages.operations.GraphOperation;
import uk.me.graphe.server.ot.GraphProcessor;

/**
 * reads messages from clients, and validates them. Sends client message pairs
 * to the message processor for transformation
 * 
 * @author Sam Phippen <samphippen@googlemail.com>
 * 
 */
public class ClientMessageHandler extends Thread {

    private ClientManager mClientManager = ClientManager.getInstance();
    private boolean mShutDown = false;
    private HeartbeatManager mHbm = new HeartbeatManager();
    private GraphProcessor mProcessor = GraphProcessor.getInstance();
    private static ClientMessageHandler sInstance = null;

    public ClientMessageHandler() {}

    @Override
    public void run() {

        while (!mShutDown) {
            Set<Client> availableClients = mClientManager
                    .waitOnReadableClients();
            for (Client c : availableClients) {
                List<String> messages = c.readNextMessages();
                System.err.println("len messages:" + messages.size());
                // if this returns null we disconnect the client for sending bad
                // messages
                List<JSONObject> jsos = validateAndParse(messages);
                
                if (jsos == null) {
                    System.err.println("disconnecting");
                    mClientManager.disconnect(c);
                } else {
                    processRequest(c, jsos);
                }
                
            }

        }

    }

    private void processRequest(Client c, List<JSONObject> jsos)
            throws Error {
        List<Message> ops;
        // malformed json == disconect
        try {
            ops = MessageFactory.makeOperationsFromJson(jsos);
            for (Message message : ops) {
                if (message.getMessage().equals("heartbeat")) {
                    mHbm.beatWhenPossible(c);
                } else if (message.getMessage().equals("makeGraph")) {
                    int id = DataManager.create();
                    c.setCurrentGraphId(id);
                    ClientMessageSender.getInstance().sendMessage(
                            c, new OpenGraphMessage(id));

                    int stateId = DataManager.getGraph(id)
                            .getStateId();
                    ClientMessageSender.getInstance().sendMessage(
                            c, new StateIdMessage(id, stateId));
                } else if (message.isOperation()) {
                    mProcessor.submit(c, (GraphOperation) message);
                } else {
                    throw new Error(
                            "got unexpected message from client");
                }
            }
        } catch (JSONException e) {
            mClientManager.disconnect(c);
        } catch (InterruptedException e) {
            mClientManager.disconnect(c);
            throw new Error(e);
        }
    }

    private List<JSONObject> validateAndParse(List<String> messages) {
        List<JSONObject> result = new ArrayList<JSONObject>();

        for (String s : messages) {
            try {
                JSONObject o = new JSONObject(s);
                result.add(o);
            } catch (JSONException e) {
                return null;
            }

        }

        return result;

    }

    public static ClientMessageHandler getInstance() {
        if (sInstance == null) sInstance = new ClientMessageHandler();
        return sInstance;
    }

}
