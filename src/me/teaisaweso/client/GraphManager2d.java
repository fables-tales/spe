package me.teaisaweso.client;

import java.util.List;

import me.teaisaweso.shared.Graph;
import me.teaisaweso.shared.Vertex;
import me.teaisaweso.shared.VertexDirection;

public interface GraphManager2d {

    /**
     * gets the underlying graph for this 2d representation
     * 
     * @return a new graph instance
     */
    public Graph getUnderlyingGraph();

    /**
     * adds a vertex to the graph
     * 
     * the vertex may not be centered at the passed location
     * 
     * @param v
     *            the vertex to be added
     * @param xPosition
     *            the center x position of the vertex
     * @param yPosition
     *            the center y position of the vertex
     */
    public void addVertex(Vertex v, int xPosition, int yPosition, int size);

    /**
     * removes a vertex from the graph
     * 
     * @param v
     *            the vertex to be removed
     */
    public void removeVertex(Vertex v);

    /**
     * moves a vertex to a given position
     * 
     * the vertex may not be centered at the passed location
     * 
     * @param v
     *            the vertex to be moved
     * @param xPosition
     *            the new center x position of the vertex
     * @param yPosition
     *            the new center y position of the vertex
     */
    public void moveVertexTo(Vertex v, int xPosition, int yPosition);

    /**
     * changes the size of a vertex
     * 
     * may displace this or other vertices
     * 
     * @param v
     *            the vertex to have it's size changed
     * @param newSize
     *            the new size of the vertex in pixels
     */
    public void scaleVertex(Vertex v, int newSize);

    /**
     * adds an edge between two vertices
     * 
     * @param v1
     *            the "from" vertex
     * @param v2
     *            the "to" vertex
     * @param dir
     *            the edge direction
     */
    public void addEdge(Vertex v1, Vertex v2, VertexDirection dir);

    /**
     * generates a list of drawable vertices
     * 
     * @return a list of VertexDrawable objects representing the vertices in
     *         this graph
     */
    public List<VertexDrawable> getVertexDrawables();

    /**
     * generates a list of drawable edges
     * 
     * @return a list of EdgeDrawable objects representing the edges in this
     *         graph
     */
    public List<EdgeDrawable> getEdgeDrawables();

}
