package uk.me.graphe.server;

import uk.me.graphe.shared.messages.Message;

public interface MessageProcessor {
    public void submit(Client c, Message m);
}
