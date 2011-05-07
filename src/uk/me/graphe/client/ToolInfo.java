package uk.me.graphe.client;

import uk.me.graphe.shared.Tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;


public class ToolInfo extends Composite {
	private static UiBinderDescription uiBinder = GWT.create(UiBinderDescription.class);
	interface UiBinderDescription extends UiBinder<Widget, ToolInfo> {}

	@UiField
	Label lblText;
	@UiField
	Button btnHelpToggle;
	
	private final Graphemeui parent;
	
	public ToolInfo(Graphemeui gUI)
	{
		initWidget(uiBinder.createAndBindUi(this));
		this.parent = gUI;
		
		btnHelpToggle.addStyleName("btnHelpOn");
		btnHelpToggle.addStyleName("btnHelpOff");
		
		btnHelpToggle.setStyleName("btnHelpOn");
		
		btnHelpToggle.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent arg0)
			{
				if (parent.isHelpEnabled)
				{
					parent.isHelpEnabled = false;
					btnHelpToggle.setStyleName("btnHelpOff");
				}
				else
				{
					parent.isHelpEnabled = true;
					btnHelpToggle.setStyleName("btnHelpOn");
				}
			}			
		});
	}
	
	public void showTool(Tools tool)
	{		
		switch(tool) {
			case addVertex:
				lblText.setText("Add Vertex (v)");
				break;
			case addEdge:
				lblText.setText("Add Edge (e)");
				break;
			case autolayout:
				lblText.setText("Auto layout");
				break;
			case cluster:
				lblText.setText("Cluster");
				break;
			case delete:
				lblText.setText("Delete (DEL)");
				break;
			case move:
				lblText.setText("Move/Pan (m)");
				break;
			case select:
				lblText.setText("Select (s)");
				break;
			case zoom:
				lblText.setText("Zoom (z)");
				break;
			case styleProcess:
				lblText.setText("Set process style");
				break;
			case styleDecision:
				lblText.setText("Set decision style");
				break;
			case styleTerminator:
				lblText.setText("Set terminator style");
				break;
			case styleNormal:
				lblText.setText("Set normal style");
				break;
		}
	}
}
