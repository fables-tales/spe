package uk.me.graphe.server.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Operation {
    private static final Map<String, OperationFactory> sOpFactoryMap = null;

	public abstract String toJson();

	public static List<Operation> makeOperationsFromJson(List<JSONObject> jsos) throws JSONException {
		
		List<Operation> result = new ArrayList<Operation>();
		
		for (JSONObject o : jsos) {
			String messageVal = o.getString("message");
			Operation op = Operation.make(messageVal, o);
			result.add(op);
		}
		
		return result;
	}

	private static Operation make(String messageVal, JSONObject o) {
		return sOpFactoryMap.get(messageVal).make(o);
	}
}
