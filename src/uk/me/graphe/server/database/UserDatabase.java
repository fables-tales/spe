package uk.me.graphe.server.database;

import java.net.UnknownHostException;
import java.util.List;

import uk.me.graphe.server.database.dbitems.UserDB;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class UserDatabase {

	private Mongo mMongo;
    private Morphia mMorphia = new Morphia();
    private Datastore mData;
    DBCollection mCollection;
    
    public UserDatabase() {
        try {
            mMongo = new Mongo();
        } catch (UnknownHostException e) {
            return;
        }
        mData = mMorphia.createDatastore(mMongo, "users");
        mCollection = mMongo.getDB("users").getCollection("UserDB");
    }
    
    public String[] getUserIDs() {
    	List<UserDB> users = mData.find(UserDB.class).asList();
    	int size = (int) mCollection.count();
    	String[] userIds = new String[size];
		int i = 0;
    	for (UserDB item : users) {
    		userIds[i] = item.getId();
    		i++;
    	}
    	return userIds;
    }
    
    public List<String> getGraphs(String userId) {
    	List<UserDB> users = mData.find(UserDB.class, "mUserId = ", userId).asList();
    	if (users == null) {
    		return null;
    	}
    	return (users.get(0)).getKeys();
    }
    
	public void newUser(String userId, String email) {
		UserDB user = new UserDB(userId, email);
		mData.save(user);
	}
	
	public void addGraph(String userId, String graphId) {
		Query<UserDB> query = mData.createQuery(UserDB.class).filter("mUserId =", userId);
		UpdateOperations<UserDB> update = mData.createUpdateOperations(UserDB.class).add("mKeys", graphId, true);
		mData.update(query, update);
	}
	
	public String getEmailFromId(String userId) {
		Query<UserDB> users = mData.find(UserDB.class,"mUserId =", userId);
		return users.get().getEmail();
		
	}
	
//	public String getIdFromEmail(String userEmail) {
//		Query<UserDB> users = mData.find(UserDB.class,"mEmail =", userEmail);
//		return users.get().getId();
//	}
	
	public void setGraphsToUsers(String email, String graphId) {
		Query<UserDB> query = mData.createQuery(UserDB.class).filter("mEmail =", email);
		UpdateOperations<UserDB> update = mData.createUpdateOperations(UserDB.class).add("mKeys", graphId, true);
		mData.update(query, update);
	}
	
	public void deleteUser(String userId) {
		mData.delete(mData.createQuery(UserDB.class).filter("mUserId =", userId));
	}

	public boolean exists(String userId, String email) {
		Query<UserDB> result;
		if (userId != null) {
			result = mData.find(UserDB.class, "mUserId =", userId);
			if (email != null)
				result = result.filter("mEmail =", email);
			if (result.asList().isEmpty())
				return false;
			else
				return true;
		}
		if (email != null) {
			result = mData.find(UserDB.class, "mEmail =", email);
			if (result.asList().isEmpty())
				return false;
			else
				return true;
		}
		return false;
	}
}
