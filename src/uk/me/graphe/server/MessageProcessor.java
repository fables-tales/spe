package uk.me.graphe.server;

import uk.me.graphe.server.messages.Message;

public interface MessageProcessor {
    public void submit(Client c, Message m);
}
