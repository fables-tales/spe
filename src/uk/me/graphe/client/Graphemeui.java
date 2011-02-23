package uk.me.graphe.client;

import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.client.json.wrapper.JSOFactory;
import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Graph;
import uk.me.graphe.shared.Vertex;
import uk.me.graphe.shared.VertexDirection;
import uk.me.graphe.shared.graphmanagers.GraphManager2d;
import uk.me.graphe.shared.graphmanagers.GraphManager2dFactory;
import uk.me.graphe.shared.jsonwrapper.JSONImplHolder;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Graphemeui implements EntryPoint {

    public Toolbox tools;
    public Canvas canvas;
    public GraphManager2dFactory graphManagerFactory;
    public GraphManager2d graphManager;
    public double zoom;
    public static final int VERTEX_SIZE = 80;
    public static final int CANVAS_HEIGHT = 400, CANVAS_WIDTH = 800;
    public static final double ZOOM_STRENGTH = 0.2;
    private int top, left;
    
    public boolean moving;
    public Vertex movingVertex;
    public final Drawing d = new DrawingImpl();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Graphemeui gui = new Graphemeui();
        JSONImplHolder.initialise(new JSOFactory());
        RootPanel.get("toolbox").add(gui.getToolBox());
        RootPanel.get("canvas").add(gui.getCanvas());
        ServerChannel sc = ServerChannel.getInstance();
        ClientOT.getInstance().setOperatingGraph(this.graphManager);
        sc.init();
        
        

        

    }

    private Widget getToolBox() {
        return tools;
    }

    private Widget getCanvas() {
        return canvas;
    }

    public Graphemeui() {
        zoom = 1;
        moving = false;
        movingVertex = null;
        tools = new Toolbox(this);
        canvas = new Canvas(this);
        graphManagerFactory = GraphManager2dFactory.getInstance();
        graphManager = graphManagerFactory.makeDefaultGraphManager();
        d.setOffset(0, 0);
        d.setZoom(zoom);
        graphManager.addRedrawCallback(new Runnable() {
            @Override
            public void run() {
                d.renderGraph(canvas.canvasPanel, graphManager
                        .getEdgeDrawables(), graphManager.getVertexDrawables());// graph
                // goes
                // here!
            }
        });

    }

    /**
     * Called by child canvas onClick(), takes in starting coordinates and the
     * end coordinates used if there was a drag. If options are required brings
     * up a dialog box in the options stack.
     */
    public void initOptions(int x1, int y1, int x2, int y2) {

        // find out which tool is selected
        int tool = tools.getTool();

        // create appropriate dialog box
        EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(), tool,
                this);

        // keep record of first point when adding nodes
        if (tool == 1) {
            ed.setPoint(x1, y1);
        }

        // show dialog
        tools.getOptionsPanel().add(ed);

        // put focus on dialog text box
        // (allows for quick typing when naming nodes)
        ed.getTextBox().setFocus(true);
    }

    public void move(int x1, int y1, int panx, int pany, int x2, int y2) {
        // if not already moving a node
        if (!moving) {
            // get vertex at point clicked at first
            VertexDrawable vd = graphManager.getDrawableAt(x1, y1);
            // if it exists user wants to move node
            if (vd != null) {
                // move the node
                moving = true;
                movingVertex = graphManager.getVertexFromDrawable(vd);
                moveNode(movingVertex, x2, y2);
            }
            // otherwise user must want to pan
            else {
                // pan
                int left = x2 - panx;
                int top = y2 - pany;
                pan(left, top);
            }
        }
        // otherwise continue moving node
        else {

            moveNode(movingVertex, x2, y2);
        }
    }

    public void moveNode(Vertex v, int x, int y) {
        if (v != null) {
            graphManager.moveVertexTo(v, x, y);
        }
    }
    
    public void zoom(boolean isZoomIn, int x, int y){
    	if(isZoomIn){
    		//work out zoom
    		zoom += ZOOM_STRENGTH;
    	} else if(zoom >= 2*ZOOM_STRENGTH){  //only zoom out if it won't scale to 0  		
    		//work out zoom
    		zoom -= ZOOM_STRENGTH;
       	}
    	
    	//work out pan
		top = (x-(int)(CANVAS_WIDTH/(2*zoom)));
		left =(y-(int)(CANVAS_HEIGHT/(2*zoom)));
		
		//move to point
		panTo(top, left);
    	
		//scale canvas
    	canvas.zoom = zoom;
		d.setZoom(zoom);
		
    	graphManager.invalidate();
    }
    
    public void panTo(int left, int top){
    	d.setOffset(-left, -top);
    	canvas.setOffset(-left, -top);
    }

    public void pan(int left, int top) {

        // update offsets in drawing and canvas
        d.setOffset(d.getOffsetX() + left, d.getOffsetY() + top);
        canvas.setOffset(canvas.getOffsetX() + left, canvas.getOffsetY() + top);

        // redraw ////////////NEEDS TO CHANGE
        graphManager.invalidate();
    }

    /**
     * called by options dialog box by OK button's onClick()
     * 
     * @param type
     *            - the type of element to add (0 = edge, 1 = remove vertex, 2 =
     *            remove edge, 3 = add vertex)
     * @param v1
     *            - the vertex to remove/add to
     * @param v2
     *            - the end vertex (if adding an edge)
     * @param edge
     *            - the edge to remove
     * @param ed
     *            - the dialog box itself (passed in so we can delete it
     *            afterwards)
     */
    public void addElement(int type, int v1, int v2, int edge, String label,
            EdgeDialog ed) {
        Graph g = graphManager.getUnderlyingGraph();
        
        if (type == 4) {
            Edge e = g.getEdges().get(edge);
            graphManager.removeEdge(e);
            ClientOT.getInstance().notifyRemoveEdge(e);
        } else if (type == 3) {
            Vertex v = g.getVertices().get(v1);
            graphManager.removeVertex(v);
            ClientOT.getInstance().notifyRemoveVertex(v);
        } else if (type == 2) {
            graphManager.addEdge(g.getVertices().get(v1), g.getVertices().get(
                    v2), VertexDirection.fromTo);
            ClientOT.getInstance().notifyAddEdge(g.getVertices().get(v1), g.getVertices().get(
                    v2), VertexDirection.fromTo);
        } else if (type == 1) {
            Vertex v = new Vertex(label);
            int[] coords = ed.getPoint();
            graphManager.addVertex(v, coords[0], coords[1], VERTEX_SIZE);
            ClientOT.getInstance().notifyAddVertex(v, coords[0], coords[1], VERTEX_SIZE);
        }
        
        removeOptions(ed);
    }

    public void removeOptions(EdgeDialog ed) {
        tools.getOptionsPanel().remove(ed);
    }

}
