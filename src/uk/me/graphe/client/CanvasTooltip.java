package uk.me.graphe.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class CanvasTooltip extends PopupPanel
{
	private final Graphemeui parent;
	private final Label lblText;
	
	public CanvasTooltip (Graphemeui gUI)
	{
		parent = gUI;
		lblText = new Label();
		
		this.add(lblText);
		
		this.setStyleName("canvasTooltip");
	}
	
	public void setText(String text)
	{
		lblText.setText(text);
	}
}