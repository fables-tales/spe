package uk.me.graphe.shared.graphmanagers;

import java.util.Collection;

import uk.me.graphe.client.EdgeDrawable;
import uk.me.graphe.client.VertexDrawable;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;


public interface GraphManager2d {

    /**
	* gets the underlying graph for this 2d representation
	*
	* @return a new graph instance
	*/
    public Graph getUnderlyingGraph();
    
    public VertexDrawable getVertexDrawable(String s);

    /**
	* adds a vertex to the graph
	*
	* the vertex may not be centered at the passed location
	*
	* @param v
	* the vertex to be added
	* @param xPosition
	* the center x position of the vertex
	* @param yPosition
	* the center y position of the vertex
	*/
    public void addVertex(Vertex v, int xPosition, int yPosition, int size);

    /**
	* removes a vertex from the graph
	*
	* @param v
	* the vertex to be removed
	*/
    public void removeVertex(Vertex v);

    /**
	* moves a vertex to a given position
	*
	* the vertex may not be centered at the passed location
	*
	* @param v
	* the vertex to be moved
	* @param xPosition
	* the new center x position of the vertex
	* @param yPosition
	* the new center y position of the vertex
	*/
    public void moveVertexTo(Vertex v, int xPosition, int yPosition);

    /**
	* changes the size of a vertex
	*
	* may displace this or other vertices
	*
	* @param v
	* the vertex to have it's size changed
	* @param newSize
	* the new size of the vertex in pixels
	*/
    public void scaleVertex(Vertex v, int newSize);

    /**
	* adds an edge between two vertices
	*
	* @param v1
	* the "from" vertex
	* @param v2
	* the "to" vertex
	* @param dir
	* the edge direction
	*/
    
    
    public void addEdge(Vertex v1, Vertex v2, VertexDirection dir, int weight);

    /**
	* removes an edge from the graphmanager
	*
	* @param e
	* the edge to be removed
	*/
    public void removeEdge(Edge e);

    /**
	* removes all the edges between v1 and v2
	*
	* @param v1
	* @param v2
	*/
    public void removeAllEdges(Vertex v1, Vertex v2);

    /**
	* generates a list of drawable vertices
	*
	* @return a list of VertexDrawable objects representing the vertices in
	* this graph
	*/
    public Collection<VertexDrawable> getVertexDrawables();

    /**
	* generates a list of drawable edges
	*
	* @return a list of EdgeDrawable objects representing the edges in this
	* graph
	*/
    public Collection<EdgeDrawable> getEdgeDrawables();

    /**
	* adds a callback for when the graph needs to be redrawn
	*
	* @param r
	*/
    public void addRedrawCallback(Runnable r);
    
	/**
	* gets the drawable at a specific position
	* @param x
	* @param y
	* @return the vertex at the location
	*/
    public VertexDrawable getVertexDrawableAt(int x, int y);
    
	/**
	* gets the drawable at a specific position
	* @param x
	* @param y
	* @return the vertex at the location
	*/
    public EdgeDrawable getEdgeDrawableAt(int x, int y);
    
    /**
     * Gets the vertex associated with the given drawable
     * @param vd
     * @return the vertex
     */
    public Vertex getVertexFromDrawable(VertexDrawable vd);

    /**
     * Gets the edge associated with the given drawable
     * @param ed
     * @return the edge
     */
    public Edge getEdgeFromDrawable(EdgeDrawable ed);
    
    /**
     * forces graph to be redrawn
     */
	public void invalidate();

    
    /**
     * checks if a vertex name is already taken
     */
    public boolean isVertexNameAvailable(String s);
    
    /**
     * says if there is already an edge between v1 and v2
     * going in the direction from v1 to v2 (takes tofrom v2 to v1 into account as well)
     * @return
     */
    public boolean isDirectedEdgeBetweenVertices(Vertex v1, Vertex v2);
    
	public boolean isEdgeBetween(Vertex vertexFromDrawable,
			Vertex vertexFromDrawable2);

    public void setVertexStyle(Vertex node, int mStyle);
    
    public String getName();
    public void setName(String name);
}
