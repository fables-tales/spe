package uk.me.graphe.server;

import java.util.Set;

import org.json.JSONObject;

import uk.me.graphe.server.ot.GraphOperation;
import uk.me.graphe.server.ot.GraphProcessor;
import uk.me.graphe.server.ot.GraphStateManager;

/**
 * reads messages from clients, and validates them.
 * Sends client message pairs to the message processor for transformation
 * @author Sam Phippen <samphippen@googlemail.com>
 *
 */
public class ClientMessageHandler extends Thread {

    private ClientManager mClientManager = ClientManager.getInstance();
    private boolean mShutDown = false;
    private GraphProcessor mProcessor;

    public ClientMessageHandler() {
    }

    @Override
    public void run() {

        while (!mShutDown) {
            Set<Client> availableClients = mClientManager.waitOnReadableClients(); 
            for (Client c : availableClients) {
                String message = c.readNextMessage();
                JSONObject jso = validateAndParse(message);
                GraphOperation op = new GraphOperation(jso);
                mProcessor.submit(c, op);
                
            }
            
        }
        
    }

}
