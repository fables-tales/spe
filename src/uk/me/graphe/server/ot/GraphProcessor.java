package uk.me.graphe.server.ot;

import uk.me.graphe.server.Client;

public interface GraphProcessor {
    public void submit(Client c, GraphOperation o);
}
