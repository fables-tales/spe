package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class SetGraphPropertiesMessage extends Message {

    private boolean mWeight;
    private boolean mDir;
    private boolean mFlowChart;

    public SetGraphPropertiesMessage(boolean weight, boolean directed, boolean flowchart) {
        mWeight = weight;
        mDir = directed;
        mFlowChart = flowchart;
    }
    
    public boolean hasWeight() {
        return mWeight;
    }
    
    public boolean hasDirection() {
        return mDir;
    }
    
    public boolean hasFlowChart() {
        return mFlowChart;
    }

    @Override
    public String toJson() {
        JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("weight", mWeight ? "yes" : "no");
            repr.put("dir", mDir ? "yes" : "no");
            repr.put("flow", mFlowChart ? "yes" : "no");
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
    }

    @Override
    public String getMessage() {
        return "sgp";
    }

}
