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


public class Description extends Composite {
	private static UiBinderDescription uiBinder = GWT.create(UiBinderDescription.class);
	interface UiBinderDescription extends UiBinder<Widget, Description> {}

	@UiField
	HorizontalPanel pnlDesc;
	@UiField
	Label lblDescription, imgSelect, imgMove, imgZoom, imgAddNode, imgAddEdge;
	
	public Description() {
		initWidget(uiBinder.createAndBindUi(this));
		pnlDesc.setVisible(false);
	}
	
	public void setTool(Tools tool) {
		
		pnlDesc.setVisible(false);
		pnlDesc.clear();
		
		switch(tool) {
			case addVertex:
				lblDescription.setText("Click the canvas to add a vertex.");
				pnlDesc.add(imgAddNode);
				pnlDesc.add(lblDescription);
				break;
			case nameVertex:
				lblDescription.setText("Type a name for the vertex in the box above.");
				pnlDesc.add(imgAddNode);
				pnlDesc.add(lblDescription);
				break;
			case addEdge:
				lblDescription.setText("Click to select the vertices you would like to connect.");
				pnlDesc.add(imgAddEdge);
				pnlDesc.add(lblDescription);
				break;
			case move:
				lblDescription.setText("Click and drag to pan the canvas or move a vertex.");
				pnlDesc.add(imgMove);
				pnlDesc.add(lblDescription);
				break;
			case select:
				lblDescription.setText("Click to select objects. Hold 'CTRL' and click to deselect or multi-select.");
				pnlDesc.add(imgSelect);
				pnlDesc.add(lblDescription);
				break;
			case zoom:
				lblDescription.setText("Click where you like to zoom in. Hold 'CTRL' and click to zoom out.");
				pnlDesc.add(imgZoom);
				pnlDesc.add(lblDescription);
				break;
			default:
				break;
		}
		
		pnlDesc.setVisible(true);
	}
}
