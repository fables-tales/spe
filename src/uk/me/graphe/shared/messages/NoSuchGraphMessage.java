package uk.me.graphe.shared.messages;


public class NoSuchGraphMessage extends Message {

    @Override
    public String getMessage() {
        return "noSuchGraph";
    }

    @Override
    public String toJson() {
        return "{\"message\":\"" + this.getMessage() + "\"}";
    }

}
