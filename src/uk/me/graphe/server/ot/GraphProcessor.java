package uk.me.graphe.server.ot;

import uk.me.graphe.server.Client;
import uk.me.graphe.server.operations.Operation;

public interface GraphProcessor {
    public void submit(Client c, Operation o);
}
