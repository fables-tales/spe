package uk.me.graphe.client;

import uk.me.graphe.graphmanagers.GraphManager2d;
import uk.me.graphe.graphmanagers.GraphManager2dFactory;
import junit.framework.Assert;
import junit.framework.TestCase;

public class GraphManager2dFactoryTest extends TestCase {
	/**
	 * verifies that the factory produces a singleton
	 */
	public void testSingleton() {
		GraphManager2dFactory f = GraphManager2dFactory.getInstance();
		GraphManager2dFactory f2 = GraphManager2dFactory.getInstance();
		// check that we only ever get one instance
		Assert.assertEquals(true, f == f2);
	}

	/**
	 * verifies that the factory product is not null
	 */
	public void testProduct() {
		GraphManager2d manager = GraphManager2dFactory.getInstance().makeDefaultGraphManager();
		Assert.assertNotNull(manager);
	}
}
