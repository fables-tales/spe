package uk.me.graphe.server.operations;

import org.json.JSONObject;

public interface OperationFactory {
	public Operation make(JSONObject o);
}
