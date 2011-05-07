package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.List;

import uk.me.graphe.client.algorithms.Djikstra;
import uk.me.graphe.client.algorithms.ShortestPathDjikstras;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import junit.framework.Assert;
import junit.framework.TestCase;

public class DjikstraTest extends TestCase{

	
	public void testDjikstra () {
		Vertex v1 = new Vertex("v1");
		Vertex v2 = new Vertex("v2");
		Edge e1 = new Edge(v1,v2,VertexDirection.fromTo);
		List<Vertex> vertices = new ArrayList<Vertex>();
		vertices.add(v1);
		vertices.add(v2);
		List<Edge> edges = new ArrayList<Edge>();
		edges.add(e1);
		Graph newGraph = new Graph(edges,vertices);
		e1.setWeight(10);
		ShortestPathDjikstras mDjikstra = new ShortestPathDjikstras();
		mDjikstra.initialise(newGraph, v1, v2);
		mDjikstra.step();
		Assert.assertEquals(mDjikstra.hasFinished(), false);
		mDjikstra.stepAll();
		String res = null;
			try {
				res = mDjikstra.getResult();
			} catch (Exception e) {
				System.out.println("Result isn't ready yet!");
			}
			
		Assert.assertEquals("10",res);
	}
}
