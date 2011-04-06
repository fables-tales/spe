package uk.me.graphe.client;

public class LocalStoreFactory {

    static LocalStoreImpl instance = new LocalStoreImpl();
	public static LocalStore newInstance() {
		return instance;
	}
}
