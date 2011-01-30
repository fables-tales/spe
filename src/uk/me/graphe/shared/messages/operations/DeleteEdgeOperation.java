package uk.me.graphe.shared.messages.operations;


import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

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
        JSONObject repr = JSONImplHolder.make();
        try {
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
        return "delEdge";
    }

}
