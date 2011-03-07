package uk.me.graphe.client;

public class LocalStoreFactory {

	public static LocalStore newInstance() {
		return new LocalStoreImpl();
	}
}
