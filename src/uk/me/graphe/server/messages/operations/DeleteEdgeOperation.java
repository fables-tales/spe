package uk.me.graphe.server.messages.operations;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.shared.Edge;

public class DeleteEdgeOperation extends EdgeOperation {

    @Override
    public boolean deletesEdge(Edge effectedEdge) {
        return effectedEdge.equals(this.getEdge());
    }

    public DeleteEdgeOperation(Edge e) {
        super(e);
    }

    @Override
    public String toJson() {
        JSONObject repr = new JSONObject();
        try {
            super.addApplyOn(repr);
            repr.put("message", "delEdge");
            repr.put("from", this.getEdge().getFromVertex().getLabel());
            repr.put("to", this.getEdge().getToVertex().getLabel());
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

    @Override
    public String getMessage() {
        // TODO Auto-generated method stub
        return null;
    }

}
