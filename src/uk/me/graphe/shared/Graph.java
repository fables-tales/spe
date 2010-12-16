package uk.me.graphe.shared;

import java.util.Collections;
import java.util.List;

public class Graph {

	private List<Edge> mEdges;

	private List<Vertex> mVertices;

	public Graph(List<Edge> edges, List<Vertex> vertices) {
		mEdges = edges;
		mVertices = vertices;
	}

	/**
	 * the edges in this graph
	 * 
	 * @return the edges in this graph
	 */
	public List<Edge> getEdges() {
		return Collections.unmodifiableList(mEdges);
	}

	/**
	 * the vertices in this graph
	 * 
	 * @return the vertices in this graph
	 */
	public List<Vertex> getVertices() {
		return Collections.unmodifiableList(mVertices);
	}

}
