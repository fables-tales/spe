package uk.me.graphe.client;

import java.util.List;

import uk.me.graphe.shared.messages.operations.GraphOperation;

public class StorePackage {

	private List<GraphOperation> mServer;
	private List<GraphOperation> mLocal;

	StorePackage(List<GraphOperation> server, List<GraphOperation> local) {
		mServer = server;
		mLocal = local;
	}
	
	public List<GraphOperation> getServer() {
		return mServer;
	}
	public List<GraphOperation> getLocal() {
		return mLocal;
	}
	

	
}
