package uk.me.graphe.client;

import java.util.Collection;

import com.google.gwt.widgetideas.graphics.client.GWTCanvas;

public interface Drawing {	
    /**
     * adds a vertex to the graph
     * 
     * the vertex may not be centered at the passed location
     * 
     * @param canvas
     *            the name of the GWTCanvas to be rendered
     * @param edges
     *            the collection of vertices to be drawn
     * @param vertices
     *            the collection of edges to be drawn
     */
    public void renderGraph (GWTCanvas canvas, Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices);

    /**
     * Assign an offset to the graph drawing. The offsets are stored 
     * in drawing and whenever anything is drawn it adds these offsets 
     * to their coordinates. 
     * @param x
     * 			the new x offset to be added to the old one
     * @param y
     * 			the new y offset to be added to the old one
     * 			
     */
    public void setOffset(int x, int y);

    /**
     * 
     * @return the x offset
     */
    public int getOffsetX();
    
    /**
     * 
     * @return the y offset
     */
    public int getOffsetY();
}
