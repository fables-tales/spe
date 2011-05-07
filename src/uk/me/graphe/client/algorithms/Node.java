package uk.me.graphe.client.algorithms;


public class Node {

    // Set starting distance to infinity
    private int distance = Integer.MAX_VALUE;
    private String id;
    private int location = 0;
    
    public Node (String name, int loc) {
        id = name;
        location = loc;
    }
    
    public void distance (int d) {
        distance = d;
        return;
    }
    
    public void location(int l) {
        location = l;
    }
    
    public int getDistance () {
        return distance;
    }
    
    public String getId () {
        return id;
    }
    
    public int getLocation () {
        return location;
    }

}
