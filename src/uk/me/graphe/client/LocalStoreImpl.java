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
    private enum State {Server, Local};
    HashMap<Integer, State> mStates = new HashMap<Integer, State>();
    HashMap<Integer, GraphOperation> mOps = new HashMap<Integer, GraphOperation>();
    LinkedList<Integer> mLocal = new LinkedList<Integer>();
    
    public LocalStoreImpl() {
        if (Storage.isSupported() == false )
            Window.alert("Warning your broswer does not support local storage, all changes made offline will not be tracked");
        mStorage = Storage.getLocalStorage();
        if (mStorage.getItem("itemID") == null || mStorage.getItem("itemID").equals("none")) {
            mStorage.clear();
        	mStorage.setItem("itemID", "none");
        } else
        	restore();
    }
    
    @Override
    public void store(GraphOperation op, boolean server) {
        //int id = op.getHistoryId();
        mOps.put(Integer.valueOf(maxHistoryId),op);
        //if (id > maxHistoryId)
        //  maxHistoryId = id;
        maxHistoryId++;
    	if (server == true)
    		toServer(op);
		else
			toLocal(op);
    	
    }
    
    @Override
    public StorePackage getInformation() {
    	List<GraphOperation> server = new ArrayList<GraphOperation>();
    	List<GraphOperation> local = new ArrayList<GraphOperation>();
    	for (int i = 0; i <= maxHistoryId; i++) {
    		Integer id = Integer.valueOf(i);
			State curState = mStates.get(id);
			if (curState == State.Server)
				server.add(mOps.get(id));
			else if (curState == State.Local)
				local.add(mOps.get(id));
    	}
    	return new StorePackage(server, local);
    }
    
    private boolean setgraph(int graphId) {
        String id = Integer.toString(graphId);
        if (mStorage.getItem("itemID").equalsIgnoreCase(id))
            return true;
        mStorage.clear();
        mStorage.setItem("itemID", id);
        mStates = new HashMap<Integer, State>();
        mOps = new HashMap<Integer, GraphOperation>();
        mLocal = new LinkedList<Integer>();
        maxHistoryId = 0;
        return false;
    }
    
    @Override
    public void save() {
    	try {
    	String maxID = Integer.toString(maxHistoryId);
    	mStorage.setItem("maxHistory", maxID);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	saveState();
    	saveOps();
    }
    
    private void saveState() {
    	int i;
    	String server = "";
    	String local = "";
    	for (i = 0; i < maxHistoryId; i++) {
			State curState = mStates.get(Integer.valueOf(i));
			if (curState == State.Server)
				server = server.concat(Integer.toString(i) + " ");
			else if (curState == State.Local)
				local = local.concat(Integer.toString(i) + " ");
		};
		mStorage.setItem("Local", local);
		mStorage.setItem("Server", server);
    }
    
    //TODO: Remove debug code
    private void saveOps(){
        //Console.log(mOps.toString());
    	for (int i = 0; i < maxHistoryId; i++) {
    		Integer id = Integer.valueOf(i);
    		GraphOperation op = mOps.get(id);
    		String jsonOp = op.toJson();
    		//Console.log(jsonOp);
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
			// Store all operations as local, map to server in restoreState()
			store((GraphOperation) item, false);
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
    	String server = mStorage.getItem("Server");
    	String local = mStorage.getItem("Local");
    	String[] st = server.split(" ");
    	for(int i = 0; i < st.length; i++)
    		mStates.put(Integer.parseInt(st[i]), State.Server);
    	st = local.split(" ");
    	for(int i = 0; i < st.length; i++) {
    		Integer j = Integer.parseInt(st[i]);
    		mStates.put(j, State.Local);
    		mLocal.add(j);
    	}
    }
    
    @Override
    public void toLocal(GraphOperation o) {
    	int historyID = o.getHistoryId();
    	mStates.put(historyID, State.Local);
    	mLocal.add(historyID);
    }
    
    @Override
    public void toServer(GraphOperation o) {
        int historyID = o.getHistoryId();
        mStates.put(historyID, State.Server);
    }

	@Override
	// Since historyId doesn't appear to be set up correctly, performing this operation may cause operations to become out of sync.
	public void setup(int GraphId, List<GraphOperation> local, List<GraphOperation> server) {
		setgraph(GraphId);
		if (server != null)
			for (GraphOperation item : server)
				store(item, true);
		if (local != null)
			for (GraphOperation item : local)
				store(item, false);;
	}

	@Override
	public void Ack() {
		for (Integer item : mLocal) {
			mStates.put(item, State.Server);
		}
		mLocal = new LinkedList<Integer>();
	}
    
}
