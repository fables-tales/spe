package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import uk.me.graphe.shared.jsonwrapper.JSONException;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;
import uk.me.graphe.shared.jsonwrapper.JSONObject;
import uk.me.graphe.shared.messages.Message;
import uk.me.graphe.shared.messages.MessageFactory;
import uk.me.graphe.shared.messages.operations.GraphOperation;

import com.google.code.gwt.storage.client.*;
import com.google.gwt.user.client.Window;

public class LocalStoreImpl implements LocalStore {
    
	int maxHistoryId = 0;
    Storage mStorage;
    private enum State {Sent, Unsent, Unacked};
    HashMap<Integer, State> mStates = new HashMap<Integer, State>();
    HashMap<Integer, GraphOperation> mOps = new HashMap<Integer, GraphOperation>();
    
    public LocalStoreImpl() {
        if (Storage.isSupported() == false )
            Window.alert("Warning your broswer does not support local storage, permanent high speed internet connection will be required to play");
        mStorage = Storage.getLocalStorage();
        if (mStorage.getItem("itemID") == null)
        	mStorage.setItem("itemID", "none");
        else
        	restore();
    }

    @Override
    public void store(GraphOperation op) {
    	int id = op.getHistoryId();
    	mOps.put(Integer.valueOf(id),op);
    	if (id > maxHistoryId)
    		maxHistoryId = id;
    	toUnsent(op);
    }
    
    @Override
    public void store(GraphOperation op, boolean Acked) {
    	int id = op.getHistoryId();
    	mOps.put(Integer.valueOf(id),op);
    	if (id > maxHistoryId)
    		maxHistoryId = id; 
    	if (Acked == true)
    		toSent(op);
		else
			toUnack(op);
    	
    }
    
    @Override
    public StorePackage getInformation() {
    	List<GraphOperation> sent = new ArrayList<GraphOperation>();
    	List<GraphOperation> unsent = new ArrayList<GraphOperation>();
    	List<GraphOperation> unacked = new ArrayList<GraphOperation>();
    	for (int i = 0; i < maxHistoryId; i++) {
    		Integer id = Integer.valueOf(i);
			State curState = mStates.get(id);
			if (curState == State.Sent)
				sent.add(mOps.get(id));
			else if (curState == State.Unacked)
				unacked.add(mOps.get(id));
			else if (curState == State.Unsent)
				unsent.add(mOps.get(id));
    	}
    	return new StorePackage(sent, unsent, unacked);
    }
    
    private boolean setgraph(int graphId) {
        String id = Integer.toString(graphId);
        if (mStorage.getItem("itemID").equalsIgnoreCase(id))
            return true;
        mStorage.clear();
        mStorage.setItem("itemID", id);
        mStates = new HashMap<Integer, State>();
        mOps = new HashMap<Integer, GraphOperation>();
        return false;
    }
    
    @Override
    public void save() {
    	String maxID = Integer.toString(maxHistoryId);
    	mStorage.setItem("maxHistory", maxID);
    	saveState();
    	saveOps();
    }
    
    private void saveState() {
    	int i;
    	String sent = "";
    	String unacked = "";
    	String unsent = "";
    	for (i = 0; i < maxHistoryId; i++) {
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
    
    private void saveOps(){
    	for (int i = 0; i < maxHistoryId; i++) {
    		Integer id = Integer.valueOf(i);
    		GraphOperation op = mOps.get(id);
    		String jsonOp = op.toJson();
    		String historyId= id.toString();
    		mStorage.setItem(historyId, jsonOp);
    	}
    }
    
    @Override
    public void restore() {
    	String max = mStorage.getItem("maxHistory");
    	maxHistoryId = Integer.parseInt(max);
    	restoreOps();
    	restoreState();
    }
    private void restoreOps() {
    	List<JSONObject> objects = new LinkedList<JSONObject>();
    	List<Message> messages = null;
    	for (int i = 0; i < maxHistoryId; i++) {
    		Integer id = Integer.valueOf(i);
    		String sId = id.toString();
    		JSONObject item = parseItem(mStorage.getItem(sId));
			objects.add(item);
    	}
    	try {
    		messages = MessageFactory.makeOperationsFromJson(objects);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Message item : messages)
			store((GraphOperation) item);
    }
    
    private JSONObject parseItem (String json) {
		JSONObject object = null;
		try {
			object = JSONImplHolder.make(json) ;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
    }
    
    private void restoreState() {
    	String sent = mStorage.getItem("Sent");
    	String unsent = mStorage.getItem("Unsent");
    	String unacked = mStorage.getItem("Unacked");
    	String[] st = sent.split(" ");
    	for(int i = 0; i < st.length; i++)
    		mStates.put(Integer.parseInt(st[i]), State.Sent);
    	st = unsent.split(" ");
    	for(int i = 0; i < st.length; i++)
    		mStates.put(Integer.parseInt(st[i]), State.Unsent);
    	st = unacked.split(" ");
    	for(int i = 0; i < st.length; i++)
    		mStates.put(Integer.parseInt(st[i]), State.Unacked);
    }
    
    @Override
    public void toUnsent(GraphOperation o) {
    	int historyID = o.getHistoryId();
    	mStates.put(historyID, State.Unsent);
    }
    
    @Override
    public void toUnack(GraphOperation o) {
        int historyID = o.getHistoryId();
        mStates.put(historyID, State.Unacked);
    }
    
    @Override
    public void toSent(GraphOperation o) {
        int historyID = o.getHistoryId();
        mStates.put(historyID, State.Sent);
    }

	@Override
	public void setup(int GraphId, List<GraphOperation> sent,
			List<GraphOperation> unsent, List<GraphOperation> unacked) {
		setgraph(GraphId);
		if (sent != null)
			for (GraphOperation item : sent)
				store(item, true);
		if (unsent != null)
			for (GraphOperation item : unsent)
				store(item);
		if (unacked != null)
			for (GraphOperation item : unacked)
				store(item, false);
	}
    
}
