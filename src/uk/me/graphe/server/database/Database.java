package uk.me.graphe.server.database;

import uk.me.graphe.shared.Graph;

public interface Database {
    
    /**
     * stores the given graph into the database
     * 
     * @param graph 
     *      the graph to be stored
     * 
     * @return a key to retrieve the graph
     */  
    public int store (Graph graph);
    
    /**
     * retrieves the graph with matching key
     * 
     * @param key 
     *      the id the graph to be retrieved
     * 
     * @return the requested graph
     */  
    public Graph retrieve (int key);
    
    /**
     * deletes the graph with matching key
     * 
     * @param key
     *      the id of the graph to be deleted
     */
    public void delete (int key);
}
