package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.shared.Edge;

public class AddEdgeOperation extends EdgeOperation {

    @Override
    public boolean createsEdge(Edge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public AddEdgeOperation(Edge e) {
        super(e);
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            super.addApplyOn(repr);
            repr.put("message", "addEdge");
            repr.put("from", this.getEdge().getFromVertex().getLabel());
            repr.put("to", this.getEdge().getToVertex().getLabel());
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

}
