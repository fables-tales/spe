package uk.me.graphe.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Dialog extends PopupPanel
{
	private final Graphemeui parent;
	private final VerticalPanel pnlCont;
	private final HorizontalPanel pnlBtns;
	private final Label lblTitle;
	private final TextBox txtParam;
	private final Button btnOk, btnCancel;
	
	private DialogType currentType;
	
	public Dialog (Graphemeui gUI)
	{
		this.parent = gUI;
		
		this.addStyleName("helpDialog");
		this.addStyleName("paramDialog");
		
		this.setAnimationEnabled(true);
				
		pnlCont = new VerticalPanel();
		pnlBtns = new HorizontalPanel();
		lblTitle = new Label();
		txtParam = new TextBox();
		btnOk = new Button("Add");
		btnCancel = new Button("Cancel");
		
		pnlBtns.add(btnOk);
		pnlBtns.add(btnCancel);
		
		btnOk.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent arg0)
			{
				switch (currentType)
				{
					case edgeWeight:
						parent.addEdge(parent.selectedVertices.get(0), parent.selectedVertices.get(1), Integer.parseInt(txtParam.getText()));
						parent.clearSelectedObjects();
						break;
					case vertexName:
						parent.addVertex(txtParam.getText());
						break;
				}
				
				parent.dialog.hide();
			}			
		});
		
		btnCancel.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent arg0)
			{
				parent.dialog.hide();
			}			
		});
		
		txtParam.addKeyDownHandler(new KeyDownHandler()
		{
			@Override
			public void onKeyDown(KeyDownEvent arg0)
			{
				switch (currentType)
				{
					case edgeWeight:
						// TODO: text validation
						break;
					case vertexName:
						// TODO: text validation
						
						break;
				}				
			}
		});
		
		txtParam.addKeyUpHandler(new KeyUpHandler()
		{
			public void onKeyUp(KeyUpEvent e)
			{
				switch (currentType)
				{
					case edgeWeight:
						if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)
						{
							btnOk.click();
						}
						break;
					case vertexName:
						if (txtParam.getText().trim().length() > 0)
						{
							btnOk.setEnabled(true);
							
							if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)
							{
								btnOk.click();
							}
						}
						else
						{
							btnOk.setEnabled(false);
						}
						break;
				}

			}
		});
		
		this.add(pnlCont);
	}

	public void show (DialogType type, String initialValue)
	{
		pnlCont.clear();
		
		currentType = type;
		
		switch (type)
		{
			case edgeWeight:
				this.setTitle("Edge weight");
				this.setAutoHideEnabled(false);
				this.setGlassEnabled(true);
				this.setPopupPosition(parent.canvas.lMouseDown[0], parent.canvas.lMouseDown[1]);
				this.setStyleName("paramDialog");
				lblTitle.setText("Edge weight:");
				txtParam.setText(initialValue);
				pnlCont.add(lblTitle);
				pnlCont.add(txtParam);
				pnlCont.add(pnlBtns);
				btnOk.setEnabled(true);
				break;
			case help:
				this.setTitle("Help");
				this.setAutoHideEnabled(true);
				this.setGlassEnabled(false);
				this.setPopupPosition(50, 125);
				this.setStyleName("helpDialog");
				lblTitle.setText(initialValue);
				pnlCont.add(lblTitle);
				break;
			case vertexName:
				this.setTitle("Vertex name");
				this.setAutoHideEnabled(false);
				this.setGlassEnabled(true);
				this.setPopupPosition(parent.canvas.lMouseDown[0], parent.canvas.lMouseDown[1]);
				this.setStyleName("paramDialog");
				lblTitle.setText("Vertex name:");
				txtParam.setText(initialValue);				
				if (initialValue.length() > 0)
				{
					btnOk.setEnabled(true);
				}
				else
				{
					btnOk.setEnabled(false);
				}				
				pnlCont.add(lblTitle);
				pnlCont.add(txtParam);
				pnlCont.add(pnlBtns);
				break;
		}
		
		this.show();
		
		txtParam.setFocus(true);
	}
}
