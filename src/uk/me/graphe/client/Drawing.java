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
    
    /**
     * set the zoom
     * @param zoom
     */
    public void setZoom(double zoom);
    
    /**
     * 
     * @return the current level of zoom (1 = no zoom)
     */
    public double getZoom();

    /**
     * 
     * @param value - determines whether user is in 
     * flow chart mode or not
     */
	public void setFlowChart(boolean value);
	
    /**
     * set the coordinates of the UI line, and displays it
     * 
     * @param startX
     *            x coordinate of start point
     * @param startY
     *            y coordinate of start point
     * @param endX
     *            x coordinate of end point
     * @param endY
     *            y coordinate of end point
     */
    public void setUILine(double startX,double startY,double endX,double endY);
    
    /**
     * displays the UI line
     * 
     */
    public void showUIline();
    
    /**
     * hides the UI line
     * 
     */
    public void hideUIline();
    
    /**
     * returns the data URL of the graph
     * 
     */
    public String getUrl();
    
    /**
     * returns the GraphML code of the graph
     * 
     */
    public String getGraphML();
}
