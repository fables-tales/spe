package uk.me.graphe.server.messages;

public abstract class Message {
    public abstract String toJson();

    public abstract String getMessage();

    public boolean isOperation() {
        return false;
    }
}
