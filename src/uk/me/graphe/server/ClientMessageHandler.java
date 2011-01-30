package uk.me.graphe.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.me.graphe.server.ot.GraphProcessor;
import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.MessageFactory;
import uk.me.graphe.shared.messages.NoSuchGraphMessage;
import uk.me.graphe.shared.messages.OpenGraphMessage;
import uk.me.graphe.shared.messages.RequestGraphMessage;
import uk.me.graphe.shared.messages.StateIdMessage;
import uk.me.graphe.shared.messages.operations.CompositeOperation;
import uk.me.graphe.shared.messages.operations.GraphOperation;


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
                if (messages != null) System.err.println("len messages:" + messages.size());
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

    private void processRequest(Client c, List<JSONObject> jsos) throws Error {
        List<Message> ops;
        // malformed json == disconect
        try {
            ops = MessageFactory.makeOperationsFromJson(jsos);
            for (Message message : ops) {
                handleMessage(c, message);
            }
        } catch (JSONException e) {
            mClientManager.disconnect(c);
        } catch (InterruptedException e) {
            return;
        }
    }

    private void handleMessage(Client c, Message message)
            throws InterruptedException, Error {
        if (message.getMessage().equals("heartbeat")) {
            mHbm.beatWhenPossible(c);
        } else if (message.getMessage().equals("makeGraph")) {
            int id = DataManager.create();
            c.setCurrentGraphId(id);
            ClientMessageSender.getInstance().sendMessage(c,
                    new OpenGraphMessage(id));

            int stateId = DataManager.getGraph(id).getStateId();
            ClientMessageSender.getInstance().sendMessage(c,
                    new CompositeOperation(new ArrayList<GraphOperation>()));
            ClientMessageSender.getInstance().sendMessage(c,
                    new StateIdMessage(id, stateId));
            c.updateStateId(stateId);
        } else if (message.getMessage().equals("requestGraph")) {
            System.err.println("got rgm");
            RequestGraphMessage rgm = (RequestGraphMessage) message;
            OTGraphManager2d g = DataManager.getGraph(rgm.getGraphId());
            c.setCurrentGraphId(rgm.getGraphId());
            if (g == null) ClientMessageSender.getInstance().sendMessage(c,
                    new NoSuchGraphMessage());
            else {
                CompositeOperation delta = g.getOperationDelta(rgm.getSince());
                ClientMessageSender.getInstance().sendMessage(c, delta);
                ClientMessageSender.getInstance().sendMessage(c,
                        new StateIdMessage(rgm.getGraphId(), g.getStateId()));
                c.updateStateId(rgm.getSince());
            }
        } else if (message.isOperation()) {
            mProcessor.submit(c, (GraphOperation) message);
        } else {
            throw new Error("got unexpected message from client");
        }
    }

    private List<JSONObject> validateAndParse(List<String> messages) {
        List<JSONObject> result = new ArrayList<JSONObject>();

        for (String s : messages) {
            try {
                System.err.println(s);
                JSONObject o = JSONImplHolder.make(s);
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

    public void shutDown() {
        mShutDown = true;
        this.interrupt();
        mClientManager.wakeUp();
    }

}
