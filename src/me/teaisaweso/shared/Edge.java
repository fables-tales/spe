package me.teaisaweso.shared;

public class Edge {
    private Vertex mVertex1;
    private Vertex mVertex2;
    private VertexDirection mDir;

    /**
     * creates an edge
     * @param v1 the "from" vertex
     * @param v2 the "to" vertex
     * @param dir the edge direction
     */
    public Edge(Vertex v1, Vertex v2, VertexDirection dir) {
        mVertex1 = v1;
        mVertex2 = v2;
        mDir = dir;
    }
    
    /**
     * determines if the edge exits a vertex
     * @param v the vertex to test against
     * @return true if this edge exits v, else false
     */
    public boolean exits(Vertex v) {
        if (mDir == VertexDirection.both && (v == mVertex1 || v == mVertex2)) return true;
        else return v ==  ((mDir == VertexDirection.fromTo) ? mVertex1 : mVertex2);
    }
    
    /**
     * determines if the edge enters a vertex
     * @param v the vertex to test against
     * @return true if this edge enters v, else false
     */
    public boolean enters(Vertex v) {
        if (mDir == VertexDirection.both && (v == mVertex1 || v == mVertex2)) return true;
        else return v ==  ((mDir == VertexDirection.toFrom) ? mVertex1 : mVertex2);
    }

}
