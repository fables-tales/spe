
package uk.me.graphe.client.dialogs;

import uk.me.graphe.client.Graphemeui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HelpDialog extends PopupPanel
{
	private static HelpDialog sInstance;
	
	private final Graphemeui parent;
	private final VerticalPanel pnlCont;
	private final Label lblText;
	
	public HelpDialog (Graphemeui gui)
	{
		this.parent = gui;
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.setTitle("Help");		
		this.setStyleName("helpDialog");
		this.setPopupPosition(50, 125);
		
		lblText = new Label();
		
		pnlCont = new VerticalPanel();
		pnlCont.add(lblText);
		
		super.add(pnlCont);
	}

	public static HelpDialog getInstance (Graphemeui gui)
	{
		if (sInstance == null) sInstance = new HelpDialog(gui);
        return sInstance;
	}
	
	public static HelpDialog getInstance ()
	{
		return sInstance;
	}
	
	public void show (String message)
	{
		if (super.isVisible()) super.hide();
		
		lblText.setText(message);
	
		super.show();		
	}
}
