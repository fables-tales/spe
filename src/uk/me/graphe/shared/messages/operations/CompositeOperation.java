package uk.me.graphe.shared.messages.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class CompositeOperation extends GraphOperation {

    private List<GraphOperation> mOperations;

    public CompositeOperation(List<GraphOperation> subList) {
        mOperations = new ArrayList<GraphOperation>(subList);
    }

    @Override
    public String toJson() {
        JSONObject result = JSONImplHolder.make();
        try {
            result.put("message", this.getMessage());

            for (GraphOperation o : mOperations) {
                result.append("values", o.toJson());
            }

            return result.toString();
        } catch (JSONException jse) {
            throw new Error(jse);
        }
    }

    public boolean deletesNode(Vertex effectedNode) {
        for (GraphOperation o : mOperations) {
            if (o.isNodeOperation()
                    && o.asNodeOperation().deletesNode(effectedNode)) {
                return true;
            }
        }

        return false;
    }

    public boolean deletesEdge(Edge effectedEdge) {
        for (GraphOperation o : mOperations) {
            if (o.isEdgeOperation()
                    && o.asEdgeOperation().deletesEdge(effectedEdge)) {
                return true;
            }
        }

        return false;
    }

    public List<GraphOperation> asIndividualOperations() {
        return Collections.unmodifiableList(mOperations);
    }

    @Override
    public String getMessage() {
        return "composite";
    }

}
