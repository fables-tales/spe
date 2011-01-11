package uk.me.graphe.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.server.GraphemeServer;
import uk.me.graphe.server.messages.Message;
import uk.me.graphe.server.messages.MessageFactory;

public class TestClient {

    private Socket mSock = new Socket();

    public void bringUpServer() {
        GraphemeServer.getInstance().start();
    }

    public void connect() {
        try {
            mSock = new Socket("localhost", 6689);
        } catch (Throwable t) {
            throw new Error(t);
        }

    }

    public void sendMessage(Message m) {
        try {
            mSock.getOutputStream().write((m.toJson() + "\0").getBytes());
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public Message readNextMessage() {
        StringBuilder sb = new StringBuilder();
        InputStream is;
        try {
            is = mSock.getInputStream();

            do {
                sb.append((char) is.read());
            } while (sb.charAt(sb.length()-1) != '\0');
            
            JSONObject jso = new JSONObject(sb.toString().substring(0,sb.length()-1));
            List<JSONObject> jsos = new ArrayList<JSONObject>();
            jsos.add(jso);
            return MessageFactory.makeOperationsFromJson(jsos).get(0);
            
        } catch (IOException e) {
            throw new Error(e);
        } catch (JSONException e) {
            throw new Error(e);
        }
    }
}
