package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.shared.messages.operations.GraphOperation;

public class StorePackage {

	private List<GraphOperation> mSent = new ArrayList<GraphOperation>();
	private List<GraphOperation> mUnsent = new ArrayList<GraphOperation>();
	private List<GraphOperation> mUnacked = new ArrayList<GraphOperation>();
	
	public StorePackage(List<GraphOperation> mSent,
			List<GraphOperation> mUnsent, List<GraphOperation> mUnacked) {
		super();
		this.mSent = mSent;
		this.mUnsent = mUnsent;
		this.mUnacked = mUnacked;
	}
	public List<GraphOperation> getSent() {
		return mSent;
	}
	public List<GraphOperation> getUnsent() {
		return mUnsent;
	}
	public List<GraphOperation> getUnacked() {
		return mUnacked;
	}
	
}
