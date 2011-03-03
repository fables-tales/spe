package uk.me.graphe.client;

import java.util.ArrayList;

import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.client.json.wrapper.JSOFactory;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Tools;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.graphmanagers.GraphManager2dFactory;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RootPanel;

public class Graphemeui implements EntryPoint {

    public final Toolbox tools;
    public final Canvas canvas;
    public final Chat chat;    
    public final GraphManager2d graphManager;
    public final GraphManager2dFactory graphManagerFactory;
    public final Drawing drawing;
    
    public ArrayList<VertexDrawable> selectedVertices;
    public ArrayList<EdgeDrawable> selectedEdges;
    
    public static final int VERTEX_SIZE = 20;
    public static final int CANVAS_HEIGHT = 800, CANVAS_WIDTH = 800;
    public static final int ZOOM_STRENGTH = 2;
    
    public boolean isHotkeysEnabled;
    
	private static final int X = 0, Y = 1;

    public Graphemeui() {
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
    	selectedVertices = new ArrayList<VertexDrawable>();
    	selectedEdges = new ArrayList<EdgeDrawable>();
    	isHotkeysEnabled = true;
    }
    
    public void onModuleLoad() {
        JSONImplHolder.initialise(new JSOFactory());
        RootPanel.get("toolbox").add(this.tools);
        RootPanel.get("canvas").add(this.canvas);
        RootPanel.get("chat").add(this.chat);
        
		KeyUpHandler khHotkeys = new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent e) {
				if (isHotkeysEnabled) {
					switch (e.getNativeKeyCode()) {
						case 69: // e
							tools.setTool(Tools.addEdge);
							break;
						case 77: // m
							tools.setTool(Tools.move);
							break;
						case 83: // s
							tools.setTool(Tools.select);
							break;
						case 86: // v
							tools.setTool(Tools.addVertex);
							break;
						case 90: // z
							tools.setTool(Tools.zoom);
							break;
						case KeyCodes.KEY_DELETE:
							// TODO: Delete what is current selected, unhighlighting them on the way.
							tools.setLabel("delete");
							break;
						default:
							break;
					}
				}
			}
		};

        RootPanel.get().addDomHandler(khHotkeys, KeyUpEvent.getType());
                
        ServerChannel sc = ServerChannel.getInstance();
        ClientOT.getInstance().setOperatingGraph(this.graphManager);
        sc.init();
    }

    
    public void addEdge(VertexDrawable from, VertexDrawable to) {
    	Vertex vFrom = graphManager.getVertexFromDrawable(from);
    	Vertex vTo = graphManager.getVertexFromDrawable(to);
        graphManager.addEdge(vFrom, vTo, VertexDirection.fromTo);
        ClientOT.getInstance().notifyAddEdge(vFrom, vTo, VertexDirection.fromTo);
        
        clearSelectedObjects();
    }
    
    public void addVertex(String label) {
        Vertex v = new Vertex(label);
        graphManager.addVertex(v, canvas.lMouseDown[X], canvas.lMouseDown[Y], VERTEX_SIZE);
        ClientOT.getInstance().notifyAddVertex(v, canvas.lMouseDown[X], canvas.lMouseDown[Y], VERTEX_SIZE);    	
    }
    
    public void clearSelectedEdges()
    {
    	for(EdgeDrawable ed : selectedEdges){
    		// TODO: Unhighlight ed here.
    	}
    	selectedEdges.clear();
    }
    
    public void clearSelectedObjects()
    {
    	clearSelectedEdges();
		clearSelectedVertices();
    }
    
    public void clearSelectedVertices()
    {
    	for(VertexDrawable vd : selectedVertices) {
    		vd.setHilighted(false);
    	}
    	
    	selectedVertices.clear();
    }
    
    public void moveNode(VertexDrawable vd, int x, int y) {
        Vertex v = graphManager.getVertexFromDrawable(vd);
        
    	if (v != null) {
            graphManager.moveVertexTo(v, x, y);
        }
    }
  
    public void pan(int left, int top) {
        drawing.setOffset(drawing.getOffsetX() + left, drawing.getOffsetY() + top);        
        graphManager.invalidate();
    }
    
    public void removeEdge(EdgeDrawable ed) {
    	Edge e = null; // TODO: Get edge from edge drawable.
        graphManager.removeEdge(e);
        ClientOT.getInstance().notifyRemoveEdge(e);
        
        if (selectedEdges.contains(ed)){
        	selectedEdges.remove(ed);
        }
    }
    
    public void removeVertex(VertexDrawable vd) {
    	Vertex v = graphManager.getVertexFromDrawable(vd);
        graphManager.removeVertex(v);
        ClientOT.getInstance().notifyRemoveVertex(v);
        
        if (selectedVertices.contains(vd)) {
        	selectedVertices.remove(vd);
        }
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
        	if (selectedVertices.contains(vd))
        	{
        		// TODO: UN-Highlight vertex here.
        		vd.setHilighted(false);
        		selectedVertices.remove(vd);
        	} else {
        		// TODO: Highlight vertex here.
        		vd.setHilighted(true);
        		selectedVertices.add(vd);
        	}
            return true;
        }
        
        return false;
    }
    
	public void zoomIn() {
		double zoom = drawing.getZoom() + ZOOM_STRENGTH;

		/**
		 * calculates left and top with respect to position of mouse click rather than
		 * middle of canvas, more natural zooming achieved.
		 * actual calculation is: relativeX - (absoluteX / newZoom) and same for y.
		 * calculated these in separate methods because you need to know previous zoom to
		 * calculate absolute positions and this changes if you're zooming in or out.
		 */

		int left = (canvas.lMouseDown[X] - (int) (((canvas.lMouseDown[X] + drawing.getOffsetX()) * (zoom - ZOOM_STRENGTH)) / zoom));
		int top = (canvas.lMouseDown[Y] - (int) (((canvas.lMouseDown[Y] + 
				drawing.getOffsetY()) * (zoom - ZOOM_STRENGTH)) / zoom));

		zoomDoAction(zoom, left, top);
	}

	public void zoomOut() {
		if (drawing.getZoom() >= (2 * ZOOM_STRENGTH)) {
			double zoom = drawing.getZoom() - ZOOM_STRENGTH;

			int left = (canvas.lMouseDown[X] - (int) (((canvas.lMouseDown[X] + drawing
					.getOffsetX()) * (zoom + ZOOM_STRENGTH)) / zoom));
			int top = (canvas.lMouseDown[Y] - (int) (((canvas.lMouseDown[Y] + drawing
					.getOffsetY()) * (zoom + ZOOM_STRENGTH)) / zoom));

			zoomDoAction(zoom, left, top);
		}
	}
    
    
    private void zoomDoAction(double zoom, int left, int top) {
        drawing.setOffset(-left, -top);

        drawing.setZoom(zoom);

        graphManager.invalidate(); // TODO: This needs to change. 	
    }
}
