package uk.me.graphe.shared.messages.operations;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

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
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", "addEdge");
            repr.put("from", this.getEdge().getFromVertex().getLabel());
            repr.put("to", this.getEdge().getToVertex().getLabel());
        } catch (JSONException e) {
            throw new Error(e);
        }
        return repr.toString();
    }

    @Override
    public String getMessage() {
        return "addEdge";
    }

    @Override
    public void applyTo(GraphManager2d mGraph) {
        mGraph.addEdge(this.getEdge().getFromVertex(), getEdge().getToVertex(),
                getEdge().getDirection());
    }

}
