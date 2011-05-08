package uk.me.graphe.client;

import java.util.ArrayList;

import uk.me.graphe.client.algorithms.AutoLayout;
import uk.me.graphe.client.algorithms.ShortestPathDjikstras;
import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.client.dialogs.EdgeDialog;
import uk.me.graphe.client.dialogs.GraphNameDialog;
import uk.me.graphe.client.dialogs.GraphOptionsDialog;
import uk.me.graphe.client.dialogs.HelpDialog;
import uk.me.graphe.client.dialogs.ShareGraphDialog;
import uk.me.graphe.client.dialogs.VertexDialog;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class Graphemeui implements EntryPoint
{   
    public final Canvas canvas;
    public final CanvasTooltip tooltip;
    public final Chat chat;  
    public final VertexDialog dialogVertex;
    public final EdgeDialog dialogEdge;
    public final GraphNameDialog dialogGraphName;
    public final ShareGraphDialog dialogShareGraph;
    public final HelpDialog dialogHelp;
    public final GraphOptionsDialog dialogGraphOptions;
    public final GraphInfo graphInfo;
    public final Toolbox tools;
    public final ToolInfo toolInfo;   
    public final GraphManager2d graphManager;
    public final GraphManager2dFactory graphManagerFactory;
    public final Drawing drawing;
    public final ShortestPathDjikstras spDjikstra;
    
    private LocalStore mStore;
    
    public ArrayList<VertexDrawable> selectedVertices;
    public ArrayList<EdgeDrawable> selectedEdges;
    
    public static final int VERTEX_SIZE = 50;
    public static final double ZOOM_STRENGTH = 0.2;
    
    public boolean isHotkeysEnabled;
    public boolean isHelpEnabled;
    
	private static final int X = 0, Y = 1;

	private AutoLayout lay;
	
	

	public Graphemeui() {
	    
        graphManagerFactory = GraphManager2dFactory.getInstance();
        graphManager = graphManagerFactory.makeDefaultGraphManager();
        graphManager.addRedrawCallback(new Runnable() {
            @Override
            public void run() {
                drawing.renderGraph(canvas.canvasPanel, graphManager.getEdgeDrawables(),
                        graphManager.getVertexDrawables());// graph
                // goes
                // here!
            }
        });
        drawing = new DrawingImpl();
        
    	dialogVertex = VertexDialog.getInstance(this);
    	dialogEdge = EdgeDialog.getInstance(this);
    	dialogHelp = HelpDialog.getInstance(this);
    	dialogGraphName = GraphNameDialog.getInstance(this);
    	dialogShareGraph = ShareGraphDialog.getInstance(this);
    	dialogGraphOptions = GraphOptionsDialog.getInstance(this);
    	toolInfo = new ToolInfo(this);
        canvas = new Canvas(this);
        chat = Chat.getInstance(this);
        graphInfo = new GraphInfo(this);
        tools = new Toolbox(this);
        tooltip = new CanvasTooltip(this);
        
        drawing.setOffset(0, 0);
        drawing.setZoom(1);     
        
    	selectedVertices = new ArrayList<VertexDrawable>();
    	selectedEdges = new ArrayList<EdgeDrawable>();
    	isHotkeysEnabled = true;
    	isHelpEnabled = true;

    	// Algorithms here
    	spDjikstra = new ShortestPathDjikstras();
    	lay = new AutoLayout(graphManager);
    }
    
    public void onModuleLoad() {
        JSONImplHolder.initialise(new JSOFactory());
        RootPanel.get("toolbox").add(this.tools);
        RootPanel.get("canvas").add(this.canvas);
        RootPanel.get("chat").add(this.chat);
        RootPanel.get("graphInfo").add(this.graphInfo);
        RootPanel.get("toolInfo").add(this.toolInfo);
        
        mStore = LocalStoreFactory.newInstance();
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
                        case 68: // d
                            //Window.open(drawing.getUrl(), "_blank", null);
                            break;
                        case 71: // g
                            //Window.prompt("DOT graph Code", GraphString.getDot(graphManager, "Grapheme",true,true));
                            break;
                        case 73: // i
                            // import graph code
                            //String graphCode = Window.prompt("DOT graph Code","");
                            //if(graphCode != null)GraphString.addDot(graphManager, graphCode);
                            // GraphString.addDot(graphManager, graphCode) will return false
                            // if error detected in code
                            break;
                        case 87:// w
                            //drawing.toggle2d();
                            //graphManager.invalidate();
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
        ClientOT.getInstance().passGraphemeUiInstance(this);
        sc.init();
    }
    
    
    public void addEdge(VertexDrawable from, VertexDrawable to, Integer weight) {
    	Vertex vFrom = graphManager.getVertexFromDrawable(from);
    	Vertex vTo = graphManager.getVertexFromDrawable(to);
    	
        graphManager.addEdge(vFrom, vTo, VertexDirection.fromTo, weight);
        ClientOT.getInstance().notifyAddEdge(vFrom, vTo, VertexDirection.fromTo, weight);		
        
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
		
		graphManager.invalidate();
    }
    
    public void clearSelectedVertices()
    {
    	for(VertexDrawable vd : selectedVertices) {
    		vd.setHilighted(false);
    	}
    	
    	selectedVertices.clear();
    }
    
    public void doAutoLayout()
    {
    	lay.initialize();
    	lay.run();
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
			e = graphManager.getEdgeFromDrawable(ed);
			graphManager.removeEdge(e);
			ClientOT.getInstance().notifyRemoveEdge(e);	
    	}
    	
    	selectedVertices.clear();
    	selectedEdges.clear(); 	
    }
    
    public void editNodeName(String name)
    {
    	//TODO: implement - edit node name locally and over OT too. Remember you need to edit the
    	// vertex and the vertex drawable label. Keep the invalidate to redraw
    	VertexDrawable vd = selectedVertices.get(0);
    	String oldLabel = vd.getLabel();
    	graphManager.renameVertex(vd.getLabel(), name);
    	ClientOT.getInstance().notifyRenameVertex(oldLabel, name);
    	clearSelectedVertices();
    	graphManager.invalidate();
    }
    
    public void editEdgeWeight(int weight)
    {
    	EdgeDrawable ed = selectedEdges.get(0);
    	graphManager.setEdgeWeight(ed, weight);
    	Edge e = graphManager.getEdgeFromDrawable(ed);
    	ClientOT.getInstance().notifyRemoveEdge(e);
    	ClientOT.getInstance().notifyAddEdge(e.getFromVertex(), e.getToVertex(), e.getDirection(), e.getWeight());
    	clearSelectedEdges();
    	graphManager.invalidate();
    }
    
    public void editGraphName(String name)
    {
    	updateGraphName(name);
    	ClientOT.getInstance().notifyNewName(name);
    }
    
    public void editGraphProperties(boolean isDigraph, boolean isFlowChart, boolean isWeighted)
    {
    	updateGraphProperties(isDigraph, isWeighted, isFlowChart);
    	ClientOT.getInstance().notifyUpdateParameters(isDigraph, isWeighted, isFlowChart);
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
    
    public void setSelectedSyle (int style, int width, int height)
    {
    	for (VertexDrawable vd : selectedVertices)
    	{
    		vd.setStyle(style);
    		vd.updateSize(width,height);
    		ClientOT.getInstance().notifyStyleChange(vd.getLabel(), style);
    	}
    	graphManager.invalidate();
    }
    
    public void toggleEdgeDirection()
    {
    	for (EdgeDrawable ed : selectedEdges)
    	{
    		// TODO: toggle the edge direction locally via Edge and EdgeDrawable and over OT.
    	}
    }
    
    public boolean toggleSelectedEdgeAt(int x, int y) {
        EdgeDrawable ed = graphManager.getEdgeDrawableAt(x, y);
        
        if (ed != null) {
        	if (selectedEdges.contains(ed))
        	{
        		ed.setHilighted(false);
        		selectedEdges.remove(ed);
        	} else {
        		ed.setHilighted(true);
        		selectedEdges.add(ed);
        	}
        	graphManager.invalidate();

            return true;
        }

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
        VertexDrawable vd = graphManager.getVertexDrawableAt(x, y);
        
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
        	
            return true;
        }

        return false;
    }
        
    public void updateGraphName(String name)
    {
    	graphManager.setName(name);
    	graphInfo.update();
    }
    
    public void updateGraphProperties(boolean isDigraph, boolean isWeighted, boolean isFlowChart)
    {
		drawing.setIsFlowChart(isFlowChart);
		drawing.setIsDigraph(isDigraph);
		drawing.setIsWeighted(isWeighted);
		graphInfo.update();
		tools.updateVisibleTools();
		graphManager.invalidate();
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
