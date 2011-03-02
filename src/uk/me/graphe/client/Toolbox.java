package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class Toolbox extends Composite {
	private static UiBinderToolbox uiBinder = GWT.create(UiBinderToolbox.class);
	interface UiBinderToolbox extends UiBinder<Widget, Toolbox> {}

	@UiField
	HorizontalPanel pnlContainer, pnlOptions, pnlTools1, pnlTools2;
	@UiField
	Button btnAddVert, btnAddEd, btnSelect, btnMove, btnZoom, btnOk, btnCancel;
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
				}
			}
		};
		
		btnAddVert.addClickHandler(chTools);
		btnAddEd.addClickHandler(chTools);
		btnSelect.addClickHandler(chTools);
		btnMove.addClickHandler(chTools);
		btnZoom.addClickHandler(chTools);
		
		ClickHandler chOptions = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(event.getSource() == btnOk) {
					switch (currentTool) {
						case nameVertex:
							parent.addVertex(txtParam.getText());
							setTool(Tools.addVertex);
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
					}
				} else {
					btnOk.setEnabled(false);
				}
			}
		});
	
		btnCancel.setText("Cancel");
		
		setTool(Tools.select);
		
		KeyUpHandler khHotkeys = new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent e) {
				if(e.isControlKeyDown()){ //use ctrl otherwise interferes with vertex naming
					switch (e.getNativeKeyCode()) {
						case 69: // e
							setTool(Tools.addEdge);
							break;
						case 77: // m
							setTool(Tools.move);
							break;
						case 83: // s
							setTool(Tools.select);
							break;
						case 86: // v
							setTool(Tools.addVertex);
							break;
						case 90: // z
							setTool(Tools.zoom);
							break;
						case KeyCodes.KEY_DELETE:
							// TODO: Delete what is current selected, unhighlighting them on the way.
							setLabel("delete");
							break;
						default:
							break;
					}
				}
			}
		};
		super.sinkEvents(Event.KEYEVENTS);
		super.addDomHandler(khHotkeys, KeyUpEvent.getType());
	}
	
	public void setTool(Tools tool) {
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
				pnlOptions.setVisible(true);
				txtParam.setFocus(true);
				break;
			case addEdge:
				lblInstruction.setText("Click to select the vertices you would like to connect.");
				pnlOptions.add(lblInstruction);
				parent.clearSelectedEdges();
				if (parent.selectedVertices.size() == 2) {
					parent.addEdge(parent.selectedVertices.get(0), parent.selectedVertices.get(1));
				} else if (parent.selectedVertices.size() > 2) {
					parent.clearSelectedVertices();
				}
				break;
			case move:
				lblInstruction.setText("Click and drag to pan the canvas or move a vertex.");
				pnlOptions.add(lblInstruction);
				break;
			case select:
				lblInstruction.setText("Click to select objects. Hold 'CTRL' and click to deselect or multi-select.");
				pnlOptions.add(lblInstruction);
				break;
			case zoom:
				lblInstruction.setText("Click where you like to zoom in. Hold 'CTRL' and click to zoom out.");
				pnlOptions.add(lblInstruction);
				break;
			default:
				break;
		}
		
		pnlOptions.setVisible(true);
	}
	
	public void setLabel(String text)
	{
		lblInstruction.setText(text);
	}
}
