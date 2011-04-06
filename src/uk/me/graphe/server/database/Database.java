package uk.me.graphe.server.database;

import uk.me.graphe.shared.graphmanagers.OTGraphManager2d;

public interface Database {
    
    /**
     * stores the given graphmanager into the database
     * 
     * @param graph 
     *      the graph to be stored
     * 
     * @return a key to retrieve the graph
     */  
    public int store (OTGraphManager2d manager);
    
    /**
     * retrieves the graph with matching key
     * 
     * @param key 
     *      the id the graph to be retrieved
     * 
     * @return the requested graph
     */  
    public OTGraphManager2d retrieve (int key);
    
    /**
     * deletes the graphmanager with matching key
     * 
     * @param key
     *      the id of the graph to be deleted
     */
    public void delete (int key);

    /**
     * removes all entries from the database
     */
    void clean();

    /**
     * returns the total number of objects in the database
     * @return number of objects
     */
    int size();
}
