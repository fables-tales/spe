package uk.me.graphe.client;

import java.util.ArrayList;

import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.client.json.wrapper.JSOFactory;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.graphmanagers.GraphManager2dFactory;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class Graphemeui implements EntryPoint {

    public final Toolbox tools;
    public final Canvas canvas;
    public final Chat chat;    
    public final GraphManager2d graphManager;
    public final GraphManager2dFactory graphManagerFactory;
    public final Drawing drawing;
    
    public ArrayList<Vertex> selectedVertices;
    public ArrayList<Edge> selectedEdges;
    
    public static final int VERTEX_SIZE = 20;
    public static final int CANVAS_HEIGHT = 800, CANVAS_WIDTH = 800;
    public static final double ZOOM_STRENGTH = 0.2;
    
	private static final int X = 0, Y = 1;

    public boolean moving;
    public Vertex movingVertex;
     

    public Graphemeui() {
    	selectedVertices = new ArrayList<Vertex>();
    	selectedEdges = new ArrayList<Edge>();
        moving = false;
        movingVertex = null;
        tools = new Toolbox(this);
        canvas = new Canvas(this);
        chat = Chat.getInstance(this);
        drawing = new DrawingImpl();
        graphManagerFactory = GraphManager2dFactory.getInstance();
        graphManager = graphManagerFactory.makeDefaultGraphManager();
        drawing.setOffset(0, 0);
        drawing.setZoom(1);
        graphManager.addRedrawCallback(new Runnable() {
            @Override
            public void run() {
                drawing.renderGraph(canvas.canvasPanel, graphManager.getEdgeDrawables(),
                        graphManager.getVertexDrawables());// graph
                // goes
                // here!
            }
        });

    }
    
    public void onModuleLoad() {;
        JSONImplHolder.initialise(new JSOFactory());
        RootPanel.get("toolbox").add(this.tools);
        RootPanel.get("canvas").add(this.canvas);
        RootPanel.get("chat").add(this.chat);
        ServerChannel sc = ServerChannel.getInstance();
        ClientOT.getInstance().setOperatingGraph(this.graphManager);
        sc.init();
    }

    
    public void addEdge(Vertex from, Vertex to) {
        graphManager.addEdge(from, to, VertexDirection.fromTo);
        ClientOT.getInstance().notifyAddEdge(from, to, VertexDirection.fromTo);
        
        clearSelectedObjects();
    }
    
    public void addVertex(String label) {
        Vertex v = new Vertex(label);
        graphManager.addVertex(v, canvas.lMouseDown[X], canvas.lMouseDown[Y], VERTEX_SIZE);
        ClientOT.getInstance().notifyAddVertex(v, canvas.lMouseDown[X], canvas.lMouseDown[Y], VERTEX_SIZE);    	
    }
    
    public void clearSelectedEdges()
    {
    	selectedEdges.clear();
    }
    
    public void clearSelectedObjects()
    {
    	// TODO: UN-Highlight vertex and edges here.
    	clearSelectedEdges();
		clearSelectedVertices();
    }
    
    public void clearSelectedVertices()
    {
    	selectedVertices.clear();
    }
    
    public void moveNode(Vertex v, int x, int y) {
        if (v != null) {
            graphManager.moveVertexTo(v, x, y);
        }
    }
  
    public void pan(int left, int top) {
        drawing.setOffset(drawing.getOffsetX() + left, drawing.getOffsetY() + top);        
        graphManager.invalidate(); // TODO: This needs to change.
    }
    
    public void removeEdge(Edge e) {
        graphManager.removeEdge(e);
        ClientOT.getInstance().notifyRemoveEdge(e);
    }
    
    public void removeVertex(Vertex v) {
        graphManager.removeVertex(v);
        ClientOT.getInstance().notifyRemoveVertex(v);   	
    }
    
    public boolean toggleSelectedEdgeAt(int x, int y) {
    	// TODO: Implement.
    	return false;
    }
    
    public boolean toggleSelectedObjectAt(int x, int y) {
    	if (toggleSelectedVertexAt(x, y))
    	{
    		return true;
    	} else if (toggleSelectedEdgeAt(x, y)) {
    		return true;
    	}
        
        return false;
    }
    
    public boolean toggleSelectedVertexAt(int x, int y) {
        VertexDrawable vd = graphManager.getDrawableAt(x, y);
        
        if (vd != null) {
        	Vertex v = graphManager.getVertexFromDrawable(vd);
        	if (selectedVertices.contains(v))
        	{
        		// TODO: UN-Highlight vertex here.
        		vd.setHilighted(false);
        		selectedVertices.remove(v);
        	} else {
        		// TODO: Highlight vertex here.
        		vd.setHilighted(true);
        		selectedVertices.add(v);
        	}
            return true;
        }
        
        return false;
    }
    
    public void zoomIn() {
    	double zoom = drawing.getZoom() + ZOOM_STRENGTH; //TODO: Need to have a condition so it doesn't zoom to high. 	
    	zoomDoAction(zoom, canvas.lMouseDown[X], canvas.lMouseDown[Y]);
    }
    
    public void zoomOut() {
    	if (drawing.getZoom() >= (2 * ZOOM_STRENGTH)) {
	    	double zoom = drawing.getZoom() - ZOOM_STRENGTH;    	
	    	zoomDoAction(zoom, canvas.lMouseDown[X], canvas.lMouseDown[Y]);
    	}
    }
    
    
    private void zoomDoAction(double zoom, int x, int y) {
        int left = (x - (int) (CANVAS_WIDTH / (2 * zoom)));
        int top = (y - (int) (CANVAS_HEIGHT / (2 * zoom)));
        
        tools.setLabel(String.valueOf(canvas.getOffsetHeight()));

        drawing.setOffset(-left, -top);

        drawing.setZoom(zoom);

        graphManager.invalidate(); // TODO: This needs to change. 	
    }
}
