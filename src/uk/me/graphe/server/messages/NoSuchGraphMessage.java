package uk.me.graphe.server.messages;

import uk.me.graphe.server.messages.operations.GraphOperation;

public class NoSuchGraphMessage extends GraphOperation {

    @Override
    public String getMessage() {
        return "noSuchGraph";
    }

    @Override
    public String toJson() {
        return "{\"message\":\"" + this.getMessage() + "\"}";
    }

}
