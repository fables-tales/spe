package uk.me.graphe.server.database.dbitems;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Id;

@Embedded("user")
public class UserDB {

	@Id
	private String mUserID;
	private List<GraphDB> mGraphs;
	
	// Void constructor for morphia
	public UserDB() {}
	
	public UserDB(String id) {
		mUserID = id;
	}
	
	public String getId() {
		return mUserID;
	}
	
	public void addGraph(GraphDB toAdd) {
		mGraphs.add(toAdd);
	}
	
	public void removeGraph(GraphDB toRemove) {
		mGraphs.remove(toRemove);
	}
	
	public List<GraphDB> getGraphs() {
		return mGraphs;
	}
}
