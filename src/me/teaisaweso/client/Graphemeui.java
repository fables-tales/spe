package me.teaisaweso.client;

import me.teaisaweso.client.graphmanagers.GraphManager2d;
import me.teaisaweso.client.graphmanagers.GraphManager2dFactory;
import me.teaisaweso.shared.Vertex;
import me.teaisaweso.shared.VertexDirection;

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
	public int nodes;
	public int VERTEX_SIZE = 50;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Graphemeui gui = new Graphemeui();
		RootPanel.get("toolbox").add(gui.getToolBox());
		RootPanel.get("canvas").add(gui.getCanvas());
	}
	
	private Widget getToolBox() {
		return tools;
	}

	private Widget getCanvas() {
		return canvas;
	}

	public Graphemeui(){
		nodes = 1;
		tools = new Toolbox(this);
		canvas = new Canvas(this);
		graphManagerFactory = GraphManager2dFactory.getInstance();
		graphManager = graphManagerFactory.makeDefaultGraphManager();
	}
	
	/**
	 * Called by child canvas onClick(), takes in starting coordinates
	 * and the end coordinates used if there was a drag.
	 * If options are required brings up a dialog box in the options stack.
	 */
	public void initOptions(int x1, int y1, int x2, int y2){
		int tool = tools.getTool();
		if(tool == 1){
			//add node tool
			EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(), 3, this);
			ed.setPoint(x1, y1);
			tools.getOptionsPanel().add(ed);
			tools.getStack().showWidget(1);
		} 
		else if(tool == 2){
			//add edge tool
			EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(), 0, this);
			tools.getOptionsPanel().add(ed);
			tools.getStack().showWidget(1);
		} 
		else if(tool == 3){
			//remove node tool
			EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(), 1, this);
			tools.getOptionsPanel().add(ed);
			tools.getStack().showWidget(1);
		}			
		else if(tool == 4){
			//remove edge tool
			EdgeDialog ed = new EdgeDialog(graphManager.getUnderlyingGraph(), 2, this);
			tools.getOptionsPanel().add(ed);
			tools.getStack().showWidget(1);
		}
	}
	
	/**
	 * called by options dialog box by OK button's
	 * onClick()
	 * 
	 * @param type - the type of element to add 
	 * (0 = edge, 1 = remove vertex, 2 = remove edge, 3 = add vertex)
	 * @param v1 - the vertex to remove/add to
	 * @param v2 - the end vertex (if adding an edge)
	 * @param edge - the edge to remove
	 * @param ed - the dialog box itself (passed in so we can delete it afterwards)
	 */
	public void addElement(int type, int v1, int v2, int edge, String label, EdgeDialog ed){
		if(type == 2){
			graphManager.removeEdge(graphManager.getUnderlyingGraph().getEdges().get(edge));
		}
		else if(type == 1){
			graphManager.removeVertex(graphManager.getUnderlyingGraph().getVertices().get(v1));
		}
		else if(type == 0){
			graphManager.addEdge(graphManager.getUnderlyingGraph().getVertices().get(v1),
					graphManager.getUnderlyingGraph().getVertices().get(v2), 
					VertexDirection.fromTo);
		}
		else if(type == 3){
			Vertex v = new Vertex(label);
			int[] coords = ed.getPoint();
			graphManager.addVertex(v, coords[0], coords[1], VERTEX_SIZE);
		}
		removeOptions(ed);
	}
	
	public void removeOptions(EdgeDialog ed){
		tools.getOptionsPanel().remove(ed);
		tools.getStack().showWidget(0);
	}

}
