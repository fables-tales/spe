package uk.me.graphe.client.graphmanagers;

public class GraphManager2dFactory {
	private static GraphManager2dFactory sInstance = null;

	/**
	 * gets an instance of a GraphManager2dfactory
	 * 
	 * @return singleton factory instance
	 */
	public static synchronized GraphManager2dFactory getInstance() {
		if (sInstance == null)
			sInstance = new GraphManager2dFactory();
		return sInstance;
	}

	/**
	 * creates an instance of the default graph manager
	 * 
	 * @return
	 */
	public GraphManager2d makeDefaultGraphManager() {
		return new GraphManager2dImpl();
	}
}
