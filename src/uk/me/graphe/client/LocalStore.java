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
        mStorage.setItem("itemID", "none");
    }
    
    public void store (GraphOperation op){
        String jsonOp = op.toJson();
        String historyId= Integer.toString(op.getHistoryId());
        mStorage.setItem(historyId, jsonOp);
    }
    
    private boolean setgraph(int graphId) {
        String id = Integer.toString(graphId);
        if (mStorage.getItem("itemID").equalsIgnoreCase(id))
            return true;
        mStorage.clear();
        mStorage.setItem("itemID", id);
        return false;
    }
}
