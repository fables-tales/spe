package me.teaisaweso.client;

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
}
