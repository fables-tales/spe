package uk.me.graphe.shared.messages.operations;


import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class DeleteNodeOperation extends NodeOperation {

    public DeleteNodeOperation(Vertex v) {
        super(v);
    }

    @Override
    public boolean deletesNode(Vertex effectedNode) {
        return effectedNode.equals(this.getNode());
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", "delNode");
            repr.put("name", this.getNode().getLabel());
        } catch (JSONException e) {
            throw new Error(e);
        }
        
        return repr.toString();

    }

    @Override
    public String getMessage() {
        return "delNode";
    }

    @Override
    public void applyTo(GraphManager2d mGraph) {
        mGraph.removeVertex(getNode());
    }

}
