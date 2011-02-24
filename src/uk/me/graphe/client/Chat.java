package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class Chat extends Composite{

	private static UiBinderChat uiBinder = GWT.create(UiBinderChat.class);

	interface UiBinderChat extends UiBinder<Widget, Chat> {}
	
	@UiField
	TextArea output, input;
	@UiField
	Button button;
	@UiField
	Label label;
	Graphemeui parent;
	
	public Chat(Graphemeui parent){
		this.parent = parent;
		initWidget(uiBinder.createAndBindUi(this));
		output.setReadOnly(true);
		button.setEnabled(false);
		
		input.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent e) {
				if (input.getText().trim().length() > 0) {
					button.setEnabled(true);
					//notify other people that user is writing e.g:
					label.setText("User is writing a message...");
				} else {
					button.setEnabled(false);
					//stop notifying e.g:
					label.setText("");
				}
				if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					button.click();
				}
			}
		});
		
		ClickHandler ch = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				//do stuff here to send message
				output.setText(output.getText() + "\nUser:\n" + input.getText());
				input.setText("");
				label.setText("");
			}
		};
		button.addClickHandler(ch);
	}
}
