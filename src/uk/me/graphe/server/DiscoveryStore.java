package uk.me.graphe.server;

import java.util.HashMap;

import org.openid4java.discovery.DiscoveryInformation;


//stores discovery information from initial discovery
//for verification purposes on return to app.

public class DiscoveryStore {

    private static HashMap<String, DiscoveryInformation> mDiscs = new HashMap<String, DiscoveryInformation>();
    
    public static void addDisc(String k, DiscoveryInformation d){
    	mDiscs.put(k, d);
    }
    
    //returns discovery information for given key AND removes from store
    //(it's one-time-only anyway.)
    public static DiscoveryInformation getDisc(String k){
    	
    	if(!mDiscs.containsKey(k)){
        	System.err.println("discovery information not found");
        	return null;
    	}
    	
    	System.err.println("getting disc " + k + " from store");
    	DiscoveryInformation d = mDiscs.get(k);
    	//Remove from store before returning
    	
    	mDiscs.remove(k);
    	
    	return d;
    }

	
}
