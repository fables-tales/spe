package uk.me.graphe.client;

import java.util.List;

import uk.me.graphe.shared.messages.operations.GraphOperation;

public interface LocalStore {

	/**
	 * Add the graph operation to the UnAcked list
	 * 
	 * @param o GraphOperation to be stored
	 */
	void toUnack(GraphOperation o);

	/**
	 * Add the graph operation to the Sent list
	 * 
	 * @param o GraphOperation to be stored
	 */
	void toSent(GraphOperation o);

	/**
	 * Add the graph operation to the Unsent list
	 * 
	 * @param o GraphOperation to be stored
	 */
	void toUnsent(GraphOperation o);

	/**
	 *  Load the state of graph into memory
	 */
	
	void restore();

	/**
	 * Prepares the localStore
	 * @param GraphId id of the graph to be stored
	 * @param sent list of server acknowledged operations
	 * @param unsent list of unsent operations
	 * @param unacked list of unacknowledged operations
	 */
	void setup(int GraphId, List<GraphOperation> sent, List<GraphOperation> unsent, List<GraphOperation> unacked);

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
	 * @param Acked Whether the server has acknowledged receiving the operation 
	 */
	void store(GraphOperation op, boolean Acked);

	/**
	 * Save the unsent operation into the Store memory
	 * @param op GraphOperation to be stored
	 */
	void store(GraphOperation op);

}
