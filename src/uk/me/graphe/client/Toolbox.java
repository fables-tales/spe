package uk.me.graphe.client;

import java.util.List;

import uk.me.graphe.shared.Edge;
import uk.me.graphe.shared.Tools;
import uk.me.graphe.shared.Vertex;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;


public class Toolbox extends Composite {
	private static UiBinderToolbox uiBinder = GWT.create(UiBinderToolbox.class);
	interface UiBinderToolbox extends UiBinder<Widget, Toolbox> {}
	
	@UiField
	HorizontalPanel pnlTools1, pnlTools2, pnlTools3, pnlTools4, pnlTools5;

	public Tools currentTool;
	
	private final Graphemeui parent;	
	private final ToolboxButton btnAddVert, btnAddEd, btnSelect, btnMove, btnZoom, btnAutoLayout, btnCluster, btnDjikstra, btnStep, btnStepAll, btnDelete, btnProcess, btnTerminator, btnDecision, btnNormal;
	
	public Toolbox(Graphemeui gUI) {
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = gUI;
		
		btnAddVert = new ToolboxButton(this, Tools.addVertex);
		btnAddEd = new ToolboxButton(this, Tools.addEdge);
		btnSelect = new ToolboxButton(this, Tools.select);
		btnMove = new ToolboxButton(this, Tools.move);
		btnZoom = new ToolboxButton(this, Tools.zoom);
		btnAutoLayout = new ToolboxButton(this, Tools.autolayout);
		btnCluster = new ToolboxButton(this, Tools.cluster);
		btnDjikstra = new ToolboxButton(this, Tools.djikstra);
		btnDelete = new ToolboxButton(this, Tools.delete);
		btnProcess = new ToolboxButton(this, Tools.styleProcess);
		btnTerminator = new ToolboxButton(this, Tools.styleTerminator);
		btnDecision = new ToolboxButton(this, Tools.styleDecision);
		btnNormal = new ToolboxButton(this, Tools.styleNormal);
		btnStep = new ToolboxButton(this, Tools.step);
		btnStepAll = new ToolboxButton(this, Tools.stepAll);
		
		pnlTools1.add(btnSelect);
		pnlTools1.add(btnMove);
		pnlTools1.add(btnZoom);
		
		pnlTools2.add(btnAddVert);
		pnlTools2.add(btnAddEd);
		pnlTools2.add(btnDelete);
		
		pnlTools3.add(btnAutoLayout);
		pnlTools3.add(btnCluster);
		pnlTools3.add(btnDjikstra);
		
		pnlTools4.add(btnProcess);
		pnlTools4.add(btnTerminator);
		pnlTools4.add(btnDecision);
		pnlTools4.add(btnNormal);
		
		pnlTools4.setVisible(false);
		
		pnlTools5.add(btnStep);
		pnlTools5.add(btnStepAll);
		
		pnlTools5.setVisible(false);
		
		setTool(Tools.select);
	}
	
	public void setTool (Tools tool)
	{
		currentTool = tool;
		parent.toolInfo.showTool(tool);

		switch (tool)
		{
			case addVertex:
				parent.clearSelectedObjects();
				parent.dialogHelp.show("Click the canvas to add a vertex.");
				break;
			case addEdge:
				parent.clearSelectedEdges();
				if (parent.selectedVertices.size() == 2)
				{
					// TODO: position this is right place.
					parent.dialogEdge.show("", 125, 125);
				}
				else if (parent.selectedVertices.size() > 2)
				{
					parent.clearSelectedVertices();
					parent.dialogHelp.show("Click to select the vertices you would like to connect.");
				}
				else
				{
					parent.dialogHelp.show("Click to select the vertices you would like to connect.");
				}
				break;
			case delete:
				parent.deleteSelected();
				this.setTool(Tools.select);
				break;
			case move:
				parent.dialogHelp.show("Click and drag to pan the canvas or move a vertex.");
				break;
			case select:
				parent.dialogHelp.show("Click to select objects. Hold 'CTRL' and click to deselect or multi-select.");
				break;
			case zoom:
				parent.dialogHelp.show("Click where you like to zoom in. Hold 'CTRL' and click to zoom out.");
				break;
			case autolayout:
				parent.doAutoLayout();
				this.setTool(Tools.select);
				break;
			case cluster:
				// TODO: Implement
				this.setTool(Tools.select);
				break;
			case djikstra:
				if(parent.selectedVertices.size() == 2)
				{
					parent.spDjikstra.initialise(parent.graphManager.getUnderlyingGraph(), 
							parent.graphManager.getVertexFromDrawable(parent.selectedVertices.get(0)), 
							parent.graphManager.getVertexFromDrawable(parent.selectedVertices.get(1)));
				}
			case step:
				parent.spDjikstra.step();
				EdgeDrawable ed;
				VertexDrawable vd;
				parent.clearSelectedObjects();
				for(Edge e : parent.spDjikstra.getHilightedEdges()){
					ed = parent.graphManager.getDrawableFromEdge(e);
					ed.setHilighted(true);
					parent.selectedEdges.add(ed);
				}
				for(Vertex v : parent.spDjikstra.getHilightedVerticies()){
					vd = parent.graphManager.getDrawableFromVertex(v);
					vd.setHilighted(true);
					parent.selectedVertices.add(vd);
				}
				if(parent.spDjikstra.hasFinished()){
					//TODO: bring up dialog
				}
			case stepAll:
				parent.spDjikstra.stepAll();
				EdgeDrawable ed1;
				VertexDrawable vd1;
				parent.clearSelectedObjects();
				for(Edge e : parent.spDjikstra.getHilightedEdges()){
					ed1 = parent.graphManager.getDrawableFromEdge(e);
					ed1.setHilighted(true);
					parent.selectedEdges.add(ed1);
				}
				for(Vertex v : parent.spDjikstra.getHilightedVerticies()){
					vd1 = parent.graphManager.getDrawableFromVertex(v);
					vd1.setHilighted(true);
					parent.selectedVertices.add(vd1);
				}
				//TODO: bring up dialog
			case styleProcess:
				parent.setSelectedSyle(VertexDrawable.STROKED_SQUARE_STYLE,
				        Graphemeui.VERTEX_SIZE*2,Graphemeui.VERTEX_SIZE);
				this.setTool(Tools.select);
				break;
			case styleTerminator:
				parent.setSelectedSyle(VertexDrawable.STROKED_TERM_STYLE,
				        Graphemeui.VERTEX_SIZE*2,Graphemeui.VERTEX_SIZE);
				this.setTool(Tools.select);
				break;
			case styleDecision:
				parent.setSelectedSyle(VertexDrawable.STROKED_DIAMOND_STYLE,
				        Graphemeui.VERTEX_SIZE*2,Graphemeui.VERTEX_SIZE);
				this.setTool(Tools.select);
				break;
			case styleNormal:
				parent.setSelectedSyle(VertexDrawable.FILLED_CIRCLE_STYLE,
				        Graphemeui.VERTEX_SIZE,Graphemeui.VERTEX_SIZE);
				this.setTool(Tools.select);
				break;
		}
	}
	
	public void showToolInfo(Tools tool)
	{
		parent.toolInfo.showTool(tool);
	}
}