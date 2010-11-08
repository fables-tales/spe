package me.teaisaweso.shared;

public class Edge {
	private Vertex mVertex1;
	private Vertex mVertex2;
	private VertexDirection mDir;
	
	public Edge(Vertex v1, Vertex v2, VertexDirection dir) {
		mVertex1 = v1;
		mVertex2 = v2;
		mDir = dir;
	}
}
