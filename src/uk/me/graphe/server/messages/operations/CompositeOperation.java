package uk.me.graphe.server.messages.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;

public class CompositeOperation extends GraphOperation {

    private List<GraphOperation> mOperations;

    public CompositeOperation(List<GraphOperation> subList) {
        mOperations = new ArrayList<GraphOperation>(subList);
    }

    @Override
    public String toJson() {
        JSONObject result = new JSONObject();
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
