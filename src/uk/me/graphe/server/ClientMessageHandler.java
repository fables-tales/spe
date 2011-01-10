package uk.me.graphe.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.MessageFactory;

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
	private MessageProcessor mProcessor;

	private static ClientMessageHandler sInstance = null;

	public ClientMessageHandler() {
	}

	@Override
	public void run() {

		while (!mShutDown) {
			Set<Client> availableClients = mClientManager
					.waitOnReadableClients();
			for (Client c : availableClients) {
				List<String> messages = c.readNextMessages();
				// if this returns null we disconnect the client for sending bad
				// messages
				List<JSONObject> jsos = validateAndParse(messages);
				if (jsos == null)
					mClientManager.disconnect(c);

				List<Message> ops;

				// malformed json == disconect
				try {
					ops = MessageFactory.makeOperationsFromJson(jsos);
					for (Message operation : ops) mProcessor.submit(c, operation);
				} catch (JSONException e) {
					mClientManager.disconnect(c);
				}
				
			}

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
		if (sInstance == null)
			sInstance = new ClientMessageHandler();
		return sInstance;
	}

}
