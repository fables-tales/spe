package uk.me.graphe.shared.messages;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;

public class ChatMessage extends Message {

	private String userId, text;
	private boolean mIsNotify, mUserWriting;
	
	public ChatMessage(String userID, String text, boolean isNotify, boolean isWriting){
		this.userId = userID;
		this.text = text;
		this.mIsNotify = isNotify;
		this.mUserWriting = isWriting;
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
            repr.put("notify", String.valueOf(mIsNotify));
            repr.put("writing", String.valueOf(mUserWriting));
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
	
	public boolean isNotification() {
		return mIsNotify;
	}
	
	public boolean isUserWriting() {
		return mUserWriting;
	}

}
