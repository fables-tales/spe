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
        if (mStorage.getItem("itemID") == null)
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
        mStorage.setItem("unAcked", "");
        mStorage.setItem("unSent", "");
        mStorage.setItem("Sent", "");
        return false;
    }
    
    public void toUnSent(GraphOperation o) {
    	int historyID = o.getHistoryId();
        String histID = Integer.toString(historyID);
        String unSent = mStorage.getItem("unSent");
        mStorage.setItem("unSent", unSent.concat(" " + histID));
    }
    
    public void toUnAck(GraphOperation o) {
        int historyID = o.getHistoryId();
        String histID = Integer.toString(historyID);
        String unAcked = mStorage.getItem("unAcked");
        String unSent = mStorage.getItem("unSent");
        if (unSent.contains(histID))
            mStorage.setItem("unSent", unSent.replace(" " + histID, ""));
        mStorage.setItem("unAcked", unAcked.concat(" " + histID));
    }
    
    public void toSent(GraphOperation o) {
        int historyID = o.getHistoryId();
        String histID = Integer.toString(historyID);
        String unAcked = mStorage.getItem("unAcked");
        String unSent = mStorage.getItem("unSent");
        String Sent = mStorage.getItem("Sent");
        if (unAcked.contains(histID))
        	mStorage.setItem("unAcked", unAcked.replace(" "+ histID, ""));
        if (unSent.contains(histID))
            mStorage.setItem("unSent", unSent.replace(" " + histID, ""));
        mStorage.setItem("Sent", Sent.concat(" " + histID));
    }
    
}
