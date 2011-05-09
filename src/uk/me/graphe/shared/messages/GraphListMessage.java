
package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class GraphListMessage extends Message{
	
	private String graphList = null;
		
	public GraphListMessage(String graphList){
		this.graphList = graphList;
	}
	
	public GraphListMessage(){
		
	}
	
	public String getGraphList(){
		return graphList;
	}
	
	@Override
	public String getMessage() {
		return "graphList";
	}

	@Override
	public String toJson() {
		JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("list", graphList);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
	}

}
