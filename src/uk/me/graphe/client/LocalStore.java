package uk.me.graphe.client;

import java.util.List;

import uk.me.graphe.shared.messages.operations.GraphOperation;

public interface LocalStore {

	/**
	 * Store the operation as a local operation
	 * 
	 * @param o GraphOperation to be stored
	 */
	void toServer(GraphOperation o);


	/**
	 * Store the operation as a server operation
	 * 
	 * @param o GraphOperation to be stored
	 */
	void toLocal(GraphOperation o);

	/**
	 *  Load the state of graph into memory
	 */
	
	void restore();

	/**
	 * Prepares the localStore
	 * @param GraphId id of the graph to be stored
	 * @param server list of server acknowledged operations
	 * @param local list of local operations
	 */
	void setup(int GraphId, List<GraphOperation> local, List<GraphOperation> server);

	/**
	 *  Save the state of the graph into the server side database
	 */
	void save();

	/**
	 * Retrieve the collection of operations being stored
	 * 
	 * @return Container for the three types of operation
	 */
	StorePackage getInformation();

	/**
	 * Save the sent operation into the Store memory
	 * 
	 * @param op GraphOperation to be stored
	 * @param server whether the operation is currently local to client or serverside, true if server side
	 */
	void store(GraphOperation op, boolean server);

}
