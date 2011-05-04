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
    
	private int maxHistoryId = 0;
    private Storage mStorage;
    private enum State {Server, Local};
    private HashMap<Integer, State> mStates = new HashMap<Integer, State>();
    private HashMap<Integer, GraphOperation> mOps = new HashMap<Integer, GraphOperation>();
    private LinkedList<Integer> mLocal = new LinkedList<Integer>();
    
    
    public LocalStoreImpl() {
        if (Storage.isSupported() == false )
            Window.alert("Warning your broswer does not support local storage, all changes made offline will not be tracked");
        mStorage = Storage.getLocalStorage();
        if (mStorage.getItem("itemID") == null) {
            mStorage.clear();
        	mStorage.setItem("itemID", "1");
        } else {
            restore();
        }
    }
    
    @Override
    public void store(GraphOperation op, boolean server) {
        mOps.put(Integer.valueOf(maxHistoryId),op);
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
    
    private void setgraph(int graphId) {
        String id = Integer.toString(graphId);
        mStorage.clear();
        mStorage.setItem("itemID", id);
        mStates = new HashMap<Integer, State>();
        mOps = new HashMap<Integer, GraphOperation>();
        mLocal = new LinkedList<Integer>();
        maxHistoryId = 0;
        mStorage.setItem("maxHistory", "0");
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
				server += Integer.toString(i) + " "; 
			else if (curState == State.Local)
				local += Integer.toString(i) + " "; 
		};
		mStorage.setItem("Local", local);
		mStorage.setItem("Server", server);
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
    	if (maxHistoryId > 0) {
    	    restoreOps();
    	    restoreState();
    	}
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
			e.printStackTrace();
		}
		int i = 0;
		for (Message item : messages) {
			// Store all operations as local, map to server in restoreState()
			mOps.put(new Integer(i), (GraphOperation) item);
			i++;
		}
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
    	String[] st;
    	if (!server.equalsIgnoreCase("")) {
        	st = server.split("\\s");
        	for(int i = 0; i < st.length; i++)
        		mStates.put(Integer.parseInt(st[i]), State.Server);
    	}
    	if (!local.equalsIgnoreCase("")) {
        	st = local.split("\\s");
        	for(int i = 0; i < st.length; i++) {
        		Integer j = Integer.parseInt(st[i]);
        		mStates.put(j, State.Local);
        		mLocal.add(j);
        	}
    	}
    }
    
    @Override
    public void toLocal(GraphOperation o) {
    	int historyID = maxHistoryId -1;
    	mStates.put(historyID, State.Local);
    	mLocal.add(historyID);
    }
    
    @Override
    public void toServer(GraphOperation o) {
        int historyID = maxHistoryId -1;
        mStates.put(historyID, State.Server);
    }

	@Override
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
    
	@Override
	public void resetServer() {
	    List<GraphOperation> local = new LinkedList<GraphOperation>();
	    for (Integer i : mLocal)
	        local.add(mOps.get(i));
	    setup(1,local,null);
	}
}
