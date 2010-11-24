package me.teaisaweso.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;


public class DrawingImpl implements Drawing {

  public void renderGraph (GWTCanvas canvas, Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices) {

    // Testing data, tests drawing function
    Collection<VertexDrawable> collection1 = new ArrayList<VertexDrawable>();
    Collection<EdgeDrawable> collection2 = new ArrayList<EdgeDrawable>();
    VertexDrawable v1 = new VertexDrawable(10,10,10,10,"v1");
    VertexDrawable v2 = new VertexDrawable(20,20,10,10,"v2");
    VertexDrawable v3 = new VertexDrawable(70,70,10,10,"v3");
    VertexDrawable v4 = new VertexDrawable(100,100,10,10,"v4");
    collection1.add(v1);
    collection1.add(v2);
    collection1.add(v3);
    collection1.add(v4);
    EdgeDrawable e1 = new EdgeDrawable(10, 10, 50, 50);
    EdgeDrawable e2 = new EdgeDrawable(100, 100, 150, 250);
    collection2.add(e1);
    collection2.add(e2);
    // End of test function
    
    // Set style of canvas
    canvas.clear();
    canvas.setLineWidth(1);
    canvas.setStrokeStyle(Color.BLACK);
    canvas.setFillStyle(Color.BLACK);
    canvas.setBackgroundColor(Color.WHITE);
    drawGraph(edges, vertices, canvas);
    
    //Adds to div specified on html
    RootPanel.get("graph_panel").add(canvas);
    
  }
  // Draws a single vertex, currently only draws circular nodes
  
  private void drawVertex(VertexDrawable vertex, GWTCanvas canvas) {
	  double centreX = vertex.getLeft() + 0.5*vertex.getWidth();
	  double centreY = vertex.getTop() + 0.5*vertex.getHeight();
	  double radius = 0.5*vertex.getWidth();
	  
	  canvas.moveTo(centreX, centreY);
      canvas.beginPath();
      canvas.arc(centreX,centreY,radius,0,360,false);
      canvas.closePath();
      canvas.stroke();
      canvas.fill();
  }
  
 // Draws a line from coordinates to other coordinates
 private void drawEdge(EdgeDrawable edge, GWTCanvas canvas) { 
	double startX = edge.getStartX();
	double startY = edge.getStartY();
	double endX = edge.getEndX();
	double endY = edge.getEndY();
	
    canvas.beginPath();
    canvas.moveTo(startX,startY);
    canvas.lineTo(endX,endY);
    canvas.closePath();
    canvas.stroke();
 }
 
 // Takes collections of edges and vertices and draws a graph on a specified canvas.
 private void drawGraph(Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices, GWTCanvas canvas) {
	 for (EdgeDrawable thisEdge : edges) {
		drawEdge(thisEdge, canvas);
	}
	 for (VertexDrawable thisVertex : vertices) {
		 drawVertex(thisVertex, canvas);
	 }
 }
}
