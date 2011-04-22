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
import uk.me.graphe.shared.messages.operations.AddNodeOperation;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class Graphemeui implements EntryPoint
{   
    public final Canvas canvas;
    public final CanvasTooltip tooltip;
    public final Chat chat;  
    public final Dialog dialog;
    public final GraphInfo graphInfo;
    public final Toolbox tools;
    public final ToolInfo toolInfo;   
    public final GraphManager2d graphManager;
    public final GraphManager2dFactory graphManagerFactory;
    public final Drawing drawing;
    
    private LocalStore mStore;
    
    public ArrayList<VertexDrawable> selectedVertices;
    public ArrayList<EdgeDrawable> selectedEdges;
    
    public static final int VERTEX_SIZE = 200;
    public static final double ZOOM_STRENGTH = 0.2;
    
    public boolean isHotkeysEnabled;
    
	private static final int X = 0, Y = 1;

    public Graphemeui() {
    	dialog = new Dialog(this);
    	toolInfo = new ToolInfo(this);
        canvas = new Canvas(this);
        chat = Chat.getInstance(this);
        graphInfo = new GraphInfo(this);
        drawing = new DrawingImpl();
        tools = new Toolbox(this);
        tooltip = new CanvasTooltip(this);
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
        RootPanel.get("graphInfo").add(this.graphInfo);
        RootPanel.get("toolInfo").add(this.toolInfo);
        
        mStore = LocalStoreFactory.newInstance();
        mStore.store(new AddNodeOperation(new Vertex("test"), 10, 10));
        Timer t = new Timer() {

            @Override
            public void run() {
                mStore.save();
            }
        };
        t.scheduleRepeating(1000);
        
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
							tools.setTool(Tools.delete);
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
    
    
    public void addEdge(VertexDrawable from, VertexDrawable to, Integer weight) {
    	Vertex vFrom = graphManager.getVertexFromDrawable(from);
    	Vertex vTo = graphManager.getVertexFromDrawable(to);
    	
    	if (weight == null)
    	{
    		// TODO: There is weight
            graphManager.addEdge(vFrom, vTo, VertexDirection.fromTo, weight);
            ClientOT.getInstance().notifyAddEdge(vFrom, vTo, VertexDirection.fromTo);	   		
    	}
    	else
    	{
    		// TODO: No weight
            graphManager.addEdge(vFrom, vTo, VertexDirection.fromTo, weight);
            ClientOT.getInstance().notifyAddEdge(vFrom, vTo, VertexDirection.fromTo);		
    	}
        
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
    		ed.setHilighted(false);
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
    	
    	tools.pnlTools4.setVisible(false);
    	
    	selectedVertices.clear();
    }
    
    public void deleteSelected()
    {
    	Vertex v;
    	
    	for (VertexDrawable vd: selectedVertices)
    	{
        	v = graphManager.getVertexFromDrawable(vd);
            graphManager.removeVertex(v);
            ClientOT.getInstance().notifyRemoveVertex(v);
    	}
    	
    	Edge e;
    	
    	for (EdgeDrawable ed: selectedEdges)
    	{
			e = null; // TODO: Get edge from edge drawable.
			graphManager.removeEdge(e);
			ClientOT.getInstance().notifyRemoveEdge(e);	
    	}
    	
    	selectedVertices.clear();
    	selectedEdges.clear();

    	graphManager.invalidate(); // TODO: does this need to be here?  	
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
        		vd.setHilighted(false);
        		selectedVertices.remove(vd);
        	} else {
        		vd.setHilighted(true);
        		selectedVertices.add(vd);
        	}
        	graphManager.invalidate();
        	
        	tools.pnlTools4.setVisible(true);
        	
            return true;
        }
        
        if (selectedVertices.isEmpty())
        {
        	tools.pnlTools4.setVisible(false);
        }

        return false;
    }
    
    public void userWentOffline(String user)
    {
    	// TODO: Implement this function and change the Server to call this function
    	// 		 when another client disconnects from the graph. This function lets the 
    	//		 chat and other things know it's happend.
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

        drawing.setOffset(-left, -top);
        drawing.setZoom(zoom);
        graphManager.invalidate();	;
	}

	public void zoomOut() {
		if (drawing.getZoom() >= (2 * ZOOM_STRENGTH)) {
			double zoom = drawing.getZoom() - ZOOM_STRENGTH;

			int left = (canvas.lMouseDown[X] - (int) (((canvas.lMouseDown[X] + drawing
					.getOffsetX()) * (zoom + ZOOM_STRENGTH)) / zoom));
			int top = (canvas.lMouseDown[Y] - (int) (((canvas.lMouseDown[Y] + drawing
					.getOffsetY()) * (zoom + ZOOM_STRENGTH)) / zoom));

	        drawing.setOffset(-left, -top);
	        drawing.setZoom(zoom);
	        graphManager.invalidate();	
		}
	}
}
