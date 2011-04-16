package uk.me.graphe.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class HelpDialog extends PopupPanel implements ClickHandler
{
	private final Label lblText;
	
	public HelpDialog ()
	{
		this.addStyleName("helpDialog");
		this.setTitle("Help");
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		this.setPopupPosition(50, 125);
				
		lblText = new Label();
		this.add(lblText);
		
		lblText.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0)
			{
				HelpDialog source = (HelpDialog)lblText.getParent();
				source.hide();
			}
		});
	}
	
	public void show (String text)
	{
		lblText.setText(text);
		this.show();
	}

	@Override
	public void onClick (ClickEvent arg0)
	{
		this.hide(true);
	}
}