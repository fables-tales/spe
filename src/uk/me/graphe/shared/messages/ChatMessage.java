package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class ChatMessage extends Message {

	private String userId, text;
	
	public ChatMessage(String userID, String text){
		this.userId = userID;
		this.text = text;
	}
	
	@Override
	public String getMessage() {
		return "chat";
	}

	@Override
	public String toJson() {
		JSONObject repr = JSONImplHolder.make();
        try {
            repr.put("message", this.getMessage());
            repr.put("id", userId);
            repr.put("text", text);
        } catch (JSONException jse) {
            throw new Error(jse);
        }
        return repr.toString();
	}

	public String getUserId() {
		return userId;
	}

	public String getText() {
		return text;
	}

}
