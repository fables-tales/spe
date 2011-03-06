package uk.me.graphe.client;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import uk.me.graphe.shared.messages.operations.GraphOperation;

import com.google.code.gwt.storage.client.*;
import com.google.gwt.user.client.Window;

public class LocalStore {
    
    Storage mStorage;
    HashMap<Integer, GraphOperation> mOps = new HashMap<Integer, GraphOperation>();
    private enum State {Sent, Unsent, Unacked};
    HashMap<Integer, State> mStates = new HashMap<Integer, State>();
    
    
    public LocalStore() {
        if (Storage.isSupported() == false )
            Window.alert("Warning your broswer does not support local storage, permanent high speed internet connection will be required to play");
        mStorage = Storage.getLocalStorage();
        if (mStorage.getItem("itemID") == null)
        	mStorage.setItem("itemID", "none");
    }
    
    private GraphOperation retrieve () {
    	String strOp = mStorage.getItem("");
    	//List<GraphOperation> op = MessageFactory.
    	
    	return null;
    }
    
    private void save (GraphOperation op){
        String jsonOp = op.toJson();
        String historyId= Integer.toString(op.getHistoryId());
        mStorage.setItem(historyId, jsonOp);
    }
    
    public void store (GraphOperation op) {
    	mOps.put(Integer.valueOf(op.getHistoryId()),op);
    	toUnsent(op);
    }
    
    public void store (GraphOperation op, boolean Acked) {
    	mOps.put(Integer.valueOf(op.getHistoryId()),op);
    	if (Acked == true)
    		toSent(op);
		else
			toUnack(op);
    	
    }
    
    private boolean setgraph(int graphId) {
        String id = Integer.toString(graphId);
        if (mStorage.getItem("itemID").equalsIgnoreCase(id))
            return true;
        mStorage.clear();
        mStorage.setItem("itemID", id);
        mStorage.setItem("unAcked", "");
        mStorage.setItem("unSent", "");
        mStorage.setItem("Sent", "");
        return false;
    }
    
    public void saveState() {
    	int i;
    	String sent = "";
    	String unacked = "";
    	String unsent = "";
    	for (i = 0; i < mStates.size(); i++) {
			State curState = mStates.get(Integer.valueOf(i));
			if (curState == State.Sent)
				sent = sent.concat(Integer.toString(i) + " ");
			else if (curState == State.Unacked)
				unacked = unacked.concat(Integer.toString(i) + " ");
			else if (curState == State.Unsent)
				unsent = unsent.concat(Integer.toString(i) + " ");
		};
		mStorage.setItem("unAcked", unacked);
		mStorage.setItem("unSent", unsent);
		mStorage.setItem("Sent", sent);
    }
    
    public void restoreState() {
    	String sent = mStorage.getItem("Sent");
    	String unsent = mStorage.getItem("Unsent");
    	String unacked = mStorage.getItem("Unacked");
    	StringTokenizer st = new StringTokenizer(sent);
    	while(st.hasMoreTokens()) {
    		String id = st.nextToken();
    		mStates.put(Integer.parseInt(id), State.Sent);
    	}
    	st = new StringTokenizer(unsent);
    	while(st.hasMoreTokens()) {
    		String id = st.nextToken();
    		mStates.put(Integer.parseInt(id), State.Unsent);
    	}
    	st = new StringTokenizer(unacked);
    	while(st.hasMoreTokens()) {
    		String id = st.nextToken();
    		mStates.put(Integer.parseInt(id), State.Unacked);
    	}
    }
    public void toUnsent(GraphOperation o) {
    	int historyID = o.getHistoryId();
    	mStates.put(historyID, State.Unsent);
//        String histID = Integer.toString(historyID);
//        String unSent = mStorage.getItem("unSent");
//        mStorage.setItem("unSent", unSent.concat(" " + histID));
    }
    
    public void toUnack(GraphOperation o) {
        int historyID = o.getHistoryId();
        mStates.put(historyID, State.Unacked);
//        String histID = Integer.toString(historyID);
//        String unAcked = mStorage.getItem("unAcked");
//        String unSent = mStorage.getItem("unSent");
//        if (unSent.contains(histID))
//            mStorage.setItem("unSent", unSent.replace(" " + histID, ""));
//        mStorage.setItem("unAcked", unAcked.concat(" " + histID));
    }
    
    public void toSent(GraphOperation o) {
        int historyID = o.getHistoryId();
        mStates.put(historyID, State.Sent);
//        String histID = Integer.toString(historyID);
//        String unAcked = mStorage.getItem("unAcked");
//        String unSent = mStorage.getItem("unSent");
//        String Sent = mStorage.getItem("Sent");
//        if (unAcked.contains(histID))
//        	mStorage.setItem("unAcked", unAcked.replace(" "+ histID, ""));
//        if (unSent.contains(histID))
//            mStorage.setItem("unSent", unSent.replace(" " + histID, ""));
//        mStorage.setItem("Sent", Sent.concat(" " + histID));
    }
    
}
