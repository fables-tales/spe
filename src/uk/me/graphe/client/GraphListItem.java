package uk.me.graphe.client;

import com.google.gwt.user.client.ui.Label;

public class GraphListItem extends Label {
	public int mID;
	public GraphListItem(String name, int id){
		this.setText(name);
		mID = id;
	}
	
	public int getID(){
		return mID;
	}
}
