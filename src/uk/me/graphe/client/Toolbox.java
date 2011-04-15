package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class Toolbox extends Composite {
	private static UiBinderToolbox uiBinder = GWT.create(UiBinderToolbox.class);
	interface UiBinderToolbox extends UiBinder<Widget, Toolbox> {}

	@UiField
	HorizontalPanel pnlOptions;
	@UiField
	Button btnAddVert, btnAddEd, btnSelect, btnMove, btnZoom, btnAutoLayout, btnCluster, btnDelete, btnProcess, btnTerminator, btnDecision, btnNormal, btnOk, btnCancel;
	@UiField
	TextBox txtParam;
	@UiField
	Label lblInstruction;

	public Tools currentTool;
	
	private final Graphemeui parent;
	
	public Toolbox(Graphemeui gUI) {
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = gUI;	

		ClickHandler chTools = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == btnSelect){
					setTool(Tools.select);
				}else if(event.getSource() == btnMove){
					setTool(Tools.move);
				}else if(event.getSource() == btnZoom){
					setTool(Tools.zoom);
				}else if(event.getSource() == btnAddVert){
					setTool(Tools.addVertex);
				}else if(event.getSource() == btnAddEd){
					setTool(Tools.addEdge);
				}else if(event.getSource() == btnDelete){
					setTool(Tools.delete);
				}else if(event.getSource() == btnAutoLayout){
					setTool(Tools.autolayout);
				}else if(event.getSource() == btnCluster){
					setTool(Tools.cluster);
				}else if(event.getSource() == btnProcess){
					for(VertexDrawable vd : parent.selectedVertices){
						vd.setStyle(VertexDrawable.STROKED_SQUARE_STYLE);
						vd.updateSize(300, 200);
					}
					parent.graphManager.invalidate();
				}else if(event.getSource() == btnDecision){
					for(VertexDrawable vd : parent.selectedVertices){
						vd.setStyle(VertexDrawable.STROKED_DIAMOND_STYLE);
						vd.updateSize(300, 200);
					}
					parent.graphManager.invalidate();
				}else if(event.getSource() == btnTerminator){
					for(VertexDrawable vd : parent.selectedVertices){
						vd.setStyle(VertexDrawable.STROKED_TERM_STYLE);
						vd.updateSize(300, 200);
					}
					parent.graphManager.invalidate();
				}else if(event.getSource() == btnNormal){
					for(VertexDrawable vd : parent.selectedVertices){
						vd.setStyle(VertexDrawable.UNDEFINED_STYLE);
						vd.updateSize(200, 200);
					}
					parent.graphManager.invalidate();
				}
			}
		};
		
		btnAddVert.addClickHandler(chTools);		
		btnAddEd.addClickHandler(chTools);
		btnSelect.addClickHandler(chTools);
		btnMove.addClickHandler(chTools);
		btnZoom.addClickHandler(chTools);
		btnAutoLayout.addClickHandler(chTools);
		btnCluster.addClickHandler(chTools);
		btnDelete.addClickHandler(chTools);
		btnDecision.addClickHandler(chTools);
		btnTerminator.addClickHandler(chTools);
		btnProcess.addClickHandler(chTools);
		btnNormal.addClickHandler(chTools);
		
		MouseOverHandler moHandler = new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent arg0) {
				Button src = (Button)arg0.getSource();
				
				if (src.equals(btnAddVert))
				{
					parent.toolInfo.showTool(Tools.addVertex);
				}
				else if (src.equals(btnAddEd))
				{
					parent.toolInfo.showTool(Tools.addEdge);
				}
				else if (src.equals(btnSelect))
				{
					parent.toolInfo.showTool(Tools.select);
				}
				else if (src.equals(btnMove))
				{
					parent.toolInfo.showTool(Tools.move);
				}
				else if (src.equals(btnZoom))
				{
					parent.toolInfo.showTool(Tools.zoom);
				}
				else if (src.equals(btnAutoLayout))
				{
					parent.toolInfo.showTool(Tools.autolayout);
				}
				else if (src.equals(btnCluster))
				{
					parent.toolInfo.showTool(Tools.cluster);
				}
				else if (src.equals(btnDelete))
				{
					parent.toolInfo.showTool(Tools.delete);
				}
				else if (src.equals(btnDecision))
				{
					parent.toolInfo.showTool(Tools.styleDecision);
				}
				else if (src.equals(btnTerminator))
				{
					parent.toolInfo.showTool(Tools.styleTerminator);
				}
				else if (src.equals(btnProcess))
				{
					parent.toolInfo.showTool(Tools.styleProcess);
				}
				else if (src.equals(btnNormal))
				{
					parent.toolInfo.showTool(Tools.styleNormal);
				}
			}
		};
		
		btnAddVert.addMouseOverHandler(moHandler);	
		btnAddEd.addMouseOverHandler(moHandler);
		btnSelect.addMouseOverHandler(moHandler);
		btnMove.addMouseOverHandler(moHandler);
		btnZoom.addMouseOverHandler(moHandler);
		btnAutoLayout.addMouseOverHandler(moHandler);
		btnCluster.addMouseOverHandler(moHandler);
		btnDelete.addMouseOverHandler(moHandler);
		btnDecision.addMouseOverHandler(moHandler);
		btnTerminator.addMouseOverHandler(moHandler);
		btnProcess.addMouseOverHandler(moHandler);
		btnNormal.addMouseOverHandler(moHandler);
		
		MouseOutHandler moutHandler = new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent arg0)
			{
				parent.toolInfo.showTool(currentTool);
			}			
		};
		
		btnAddVert.addMouseOutHandler(moutHandler);	
		btnAddEd.addMouseOutHandler(moutHandler);	
		btnSelect.addMouseOutHandler(moutHandler);	
		btnMove.addMouseOutHandler(moutHandler);	
		btnZoom.addMouseOutHandler(moutHandler);	
		btnAutoLayout.addMouseOutHandler(moutHandler);	
		btnCluster.addMouseOutHandler(moutHandler);	
		btnDelete.addMouseOutHandler(moutHandler);	
		btnDecision.addMouseOutHandler(moutHandler);	
		btnTerminator.addMouseOutHandler(moutHandler);	
		btnProcess.addMouseOutHandler(moutHandler);	
		btnNormal.addMouseOutHandler(moutHandler);	
		
		ClickHandler chOptions = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == btnOk) {
					switch (currentTool) {
						case nameVertex:
							parent.addVertex(txtParam.getText());
							setTool(Tools.addVertex);
							break;
						case weightEdge:
							//TODO: Set weight of edge here
							try
							{
								Integer.parseInt(txtParam.getText());
							}
							catch (NumberFormatException e)
							{
								DialogBox db = new DialogBox();
								db.setTitle("GayBox");
								db.setText("This is gay");
								db.center();
								//db.show();
								//db.hide(true);
								db.setAnimationEnabled(true);
								db.setGlassEnabled(true);
								db.setAutoHideEnabled(true);
								//db.setPixelSize(300, 300);
								db.show();
								break;
							}
							parent.addEdge(parent.selectedVertices.get(0), parent.selectedVertices.get(1), 0);
							parent.clearSelectedObjects();
							setTool(Tools.addEdge);
							break;
						default:
							break;
					}
				} else {
					setTool(Tools.select);
				}
			}
		};
		
		btnOk.addClickHandler(chOptions);
		btnCancel.addClickHandler(chOptions);
		
		txtParam.setWidth("30");

		txtParam.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent e) {
				if (txtParam.getText().trim().length() > 0) {
					btnOk.setEnabled(true);
					
					if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						btnOk.click();
						pnlOptions.setVisible(false);
						//TODO: Not sure this is needed
						/*
						switch (currentTool) {
							case nameVertex:
								setTool(Tools.addVertex);
								break;
							case weightEdge:
								setTool(Tools.addEdge);
								break;
						}*/
					}
				} else if (currentTool != Tools.weightEdge) {
					btnOk.setEnabled(false);
				}
			}
		});
	
		btnCancel.setText("Cancel");
		
		setTool(Tools.select);
	}
	
	public void setTool(Tools tool) {
		parent.isHotkeysEnabled = true;
		currentTool = tool;
		
		pnlOptions.setVisible(false);
		pnlOptions.clear();
		switch(tool) {
			case addVertex:
				lblInstruction.setText("Click the canvas to add a vertex.");
				pnlOptions.add(lblInstruction);
				parent.clearSelectedObjects();
				break;
			case nameVertex:
				lblInstruction.setText("Vertex name:");
				pnlOptions.add(lblInstruction);
				txtParam.setText("");
				pnlOptions.add(txtParam);
				btnOk.setEnabled(false);
				btnOk.setText("Add");
				pnlOptions.add(btnOk);
				pnlOptions.add(btnCancel);
				parent.isHotkeysEnabled = false;
				break;
			case addEdge:
				lblInstruction.setText("Click to select the vertices you would like to connect.");
				pnlOptions.add(lblInstruction);
				parent.clearSelectedEdges();
				if (parent.selectedVertices.size() == 2) {
					setTool(Tools.weightEdge);
				} else if (parent.selectedVertices.size() > 2) {
					parent.clearSelectedVertices();
				}
				break;
			case weightEdge:
				lblInstruction.setText("Edge weight:");
				pnlOptions.add(lblInstruction);
				txtParam.setText("");
				pnlOptions.add(txtParam);
				btnOk.setEnabled(true);
				btnOk.setText("Add");
				pnlOptions.add(btnOk);
				pnlOptions.add(btnCancel);
				parent.isHotkeysEnabled = false;
				break;
			case autolayout:
				// TODO: Implement
				break;
			case cluster:
				// TODO: Implment
				break;
			case delete:
				// TODO: Implement
				parent.deleteSelected();
				break;
			case move:
				lblInstruction.setText("Click and drag to pan the canvas or move a vertex.");
				pnlOptions.add(lblInstruction);
				break;
			case select:
				lblInstruction.setText("Click to select objects. Hold 'CTRL' and click to deselect or multi-select.");
				pnlOptions.add(lblInstruction);
				parent.toolInfo.showTool(Tools.select);
				break;
			case zoom:
				lblInstruction.setText("Click where you like to zoom in. Hold 'CTRL' and click to zoom out.");
				pnlOptions.add(lblInstruction);
				break;
			default:
				break;
		}
		
		parent.toolInfo.showTool(tool);
		
		pnlOptions.setVisible(true);
		if (tool == Tools.nameVertex || tool == Tools.addEdge) txtParam.setFocus(true);
		
	}
	
	public void setLabel(String text)
	{
		lblInstruction.setText(text);
	}
}
