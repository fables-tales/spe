package uk.me.graphe.client;

import java.util.List;

import uk.me.graphe.shared.messages.operations.GraphOperation;

import com.google.code.gwt.storage.client.*;
import com.google.gwt.user.client.Window;

public class LocalStore {
    
    Storage mStorage;
    List<GraphOperation> mOps;
    
    public LocalStore() {
        if (Storage.isSupported() == false )
            Window.alert("Warning your broswer does not support local storage, permanent high speed internet connection will be required to play");
        mStorage = Storage.getLocalStorage();
        if (mStorage.getItem("itemID") == null) {
            mStorage.setItem("itemID", "none");
            mStorage.setItem("historyID", "0");
        }
    }
    
    public void store (GraphOperation op){
        String jsonOp = op.toJson();
        int newHistId = op.getHistoryId();
        String historyId= Integer.toString(newHistId);
        mStorage.setItem(historyId, jsonOp);
        int oldHistId = Integer.parseInt(mStorage.getItem("historyID"));
        if (newHistId > oldHistId) {
            mStorage.setItem("historyID", historyId);
        }
        
    }
    
    
    private boolean setgraph(int graphId) {
        String id = Integer.toString(graphId);
        if (mStorage.getItem("itemID").equalsIgnoreCase(id))
            return true;
        mStorage.clear();
        mStorage.setItem("itemID", id);
        mStorage.setItem("historyID", "0");
        return false;
    }
}
