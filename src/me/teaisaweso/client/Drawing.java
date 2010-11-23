package me.teaisaweso.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;


public class Drawing {

  public void renderGraph (GWTCanvas mCanvas) {

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
    mCanvas.setLineWidth(1);
    mCanvas.setStrokeStyle(Color.BLACK);
    mCanvas.setFillStyle(Color.BLACK);
    mCanvas.setBackgroundColor(Color.WHITE);
    drawGraph(collection2, collection1, mCanvas);
    
    //Adds to div specified on html
    RootPanel.get("graph_panel").add(mCanvas);
    
  }
  // Draws a single vertex, currently only draws circular nodes
  
  private void drawVertex(VertexDrawable mVertex, GWTCanvas mCanvas) {
	  double centreX = mVertex.getLeft() + 0.5*mVertex.getWidth();
	  double centreY = mVertex.getTop() + 0.5*mVertex.getHeight();
	  double radius = 0.5*mVertex.getWidth();
	  
	  mCanvas.moveTo(centreX, centreY);
      mCanvas.beginPath();
      mCanvas.arc(centreX,centreY,radius,0,360,false);
      mCanvas.closePath();
      mCanvas.stroke();
      mCanvas.fill();
  }
  
 // Draws a line from coordinates to other coordinates
 private void drawEdge(EdgeDrawable mEdge, GWTCanvas mCanvas) { 
	double mStartX = mEdge.getStartX();
	double mStartY = mEdge.getStartY();
	double mEndX = mEdge.getEndX();
	double mEndY = mEdge.getEndY();
	
    mCanvas.beginPath();
    mCanvas.moveTo(mStartX,mStartY);
    mCanvas.lineTo(mEndX,mEndY);
    mCanvas.closePath();
    mCanvas.stroke();
 }
 
 // Takes collections of edges and vertices and draws a graph on a specified canvas.
 private void drawGraph(Collection<EdgeDrawable> mEdges, Collection<VertexDrawable> mVertices, GWTCanvas canvas) {
	 for (EdgeDrawable thisEdge : edges) {
		drawEdge(thisEdge, canvas);
	}
	 for (VertexDrawable thisVertex : vertices) {
		 drawVertex(thisVertex, canvas);
	 }
 }
}
