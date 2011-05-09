package uk.me.graphe.shared;

import java.util.ArrayList;

public class User {
	private int id;
    private String email;
    private String openId;
    private boolean authd = false;
    private ArrayList<Integer> eGraphs; //IDs of graphs editable by this user

    /**
     * creates a new user
     * 
     * @param i
     *            ID for local use
     * @param e
     *            user email address
     * @param oi
     * 			  user's openID
     */
    public User(int i, String e, String oi) {
    	id = i;
    	email = e;
    	openId = oi;
    	eGraphs = new ArrayList<Integer>();
    }
    
    /**
     * adds permissions for a graph for this user
     * 
     * @param gId
     *            local ID for local use
     * @param perLvl
     *            user email address
     */
    public void authorise(int gId) {
		eGraphs.add(gId);
    }
    
    /**
     * gets permissions for a graph for this user
     * 
     * @param gId
     *            local ID for local use
     * @return
     *            permission level
     */

    
    public int getId(){
    	return id;
    }
    
    public String getEmail(){
    	return email;
    }
    
    public String getOpenId(){
    	return openId;
    }
    
    public boolean isAuthd(){
    	return authd;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Integer) {
            int cmp = (Integer)o;
            return cmp == id;
        } else return false;
    }

}
