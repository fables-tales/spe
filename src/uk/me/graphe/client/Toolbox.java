package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

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
	HorizontalPanel pnlTools1, pnlTools2, pnlTools3, pnlTools4;

	public Tools currentTool;
	
	private final Graphemeui parent;	
	private final ToolboxButton btnAddVert, btnAddEd, btnSelect, btnMove, btnZoom, btnAutoLayout, btnCluster, btnDelete, btnProcess, btnTerminator, btnDecision, btnNormal;
	
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
		btnDelete = new ToolboxButton(this, Tools.delete);
		btnProcess = new ToolboxButton(this, Tools.styleProcess);
		btnTerminator = new ToolboxButton(this, Tools.styleTerminator);
		btnDecision = new ToolboxButton(this, Tools.styleDecision);
		btnNormal = new ToolboxButton(this, Tools.styleNormal);
		
		pnlTools1.add(btnSelect);
		pnlTools1.add(btnMove);
		pnlTools1.add(btnZoom);
		
		pnlTools2.add(btnAddVert);
		pnlTools2.add(btnAddEd);
		pnlTools2.add(btnDelete);
		
		pnlTools3.add(btnAutoLayout);
		pnlTools3.add(btnCluster);
		
		pnlTools4.add(btnProcess);
		pnlTools4.add(btnTerminator);
		pnlTools4.add(btnDecision);
		pnlTools4.add(btnNormal);
		
		pnlTools4.setVisible(false);
		
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
				parent.dialog.show(DialogType.help, "Click the canvas to add a vertex.", 50, 125);
				break;
			case addEdge:
				parent.clearSelectedEdges();
				if (parent.selectedVertices.size() == 2)
				{
					// TODO: position this is right place.
					parent.dialog.show(DialogType.edgeWeight, "", 125, 125);
				}
				else if (parent.selectedVertices.size() > 2)
				{
					parent.clearSelectedVertices();
					parent.dialog.show(DialogType.help, "Click to select the vertices you would like to connect.", 50, 125);
				}
				else
				{
					parent.dialog.show(DialogType.help, "Click to select the vertices you would like to connect.", 50, 125);
				}
				break;
			case delete:
				parent.deleteSelected();
				this.setTool(Tools.select);
				break;
			case move:
				parent.dialog.show(DialogType.help, "Click and drag to pan the canvas or move a vertex.", 50, 125);
				break;
			case select:
				parent.dialog.show(DialogType.help, "Click to select objects. Hold 'CTRL' and click to deselect or multi-select.", 50, 125);
				break;
			case zoom:
				parent.dialog.show(DialogType.help, "Click where you like to zoom in. Hold 'CTRL' and click to zoom out.", 50, 125);
				break;
			case autolayout:
				parent.doAutoLayout();
				this.setTool(Tools.select);
				break;
			case cluster:
				// TODO: Implement
				this.setTool(Tools.select);
				break;
			case styleProcess:
				parent.setSelectedSyle(VertexDrawable.STROKED_SQUARE_STYLE);
				this.setTool(Tools.select);
				break;
			case styleTerminator:
				parent.setSelectedSyle(VertexDrawable.STROKED_TERM_STYLE);
				this.setTool(Tools.select);
				break;
			case styleDecision:
				parent.setSelectedSyle(VertexDrawable.STROKED_DIAMOND_STYLE);
				this.setTool(Tools.select);
				break;
			case styleNormal:
				parent.setSelectedSyle(VertexDrawable.FILLED_CIRCLE_STYLE);
				this.setTool(Tools.select);
				break;
		}
	}
	
	public void showToolInfo(Tools tool)
	{
		parent.toolInfo.showTool(tool);
	}
}