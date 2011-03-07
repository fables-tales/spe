package uk.me.graphe.server.database.dbitems;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Id;

@Embedded("user")
public class UserDB {

	@Id
	private String mUserID;
	private List<String> mKeys;
	
	// Void constructor for morphia
	public UserDB() {}
	
	public UserDB(String id) {
		mUserID = id;
	}
	
	public String getId() {
		return mUserID;
	}
	
	public void addKey(String toAdd) {
		mKeys.add(toAdd);
	}
	
	public void removeKey(GraphDB toRemove) {
		mKeys.remove(toRemove);
	}
	
	public List<String> getKeys() {
		return mKeys;
	}
}
