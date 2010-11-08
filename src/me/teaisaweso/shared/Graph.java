package me.teaisaweso.shared;

import java.util.List;

public interface Graph {
	/**
	 * the edges in this graph
	 * @return the edges in this graph
	 */
	public List<Edge> getEdges();
	
	/**
	 * the vertices in this graph
	 * @return the vertices in this graph
	 */
	public List<Vertex> getVertices();
	
}
