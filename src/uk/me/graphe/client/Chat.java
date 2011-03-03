package uk.me.graphe.client;

import java.util.Random;

import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.shared.messages.ChatMessage;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Chat extends Composite{

	private static UiBinderChat uiBinder = GWT.create(UiBinderChat.class);
	
	private static int mMyName = new Random().nextInt(10000);
	
	private boolean mSendEnabled;

	interface UiBinderChat extends UiBinder<Widget, Chat> {}
	
	private static Chat sInstance;
	
	@UiField
	TextArea output;
	@UiField
	TextArea input;
	@UiField
	Button minimize;
	@UiField
	VerticalPanel panel;
	Graphemeui parent;
	
	public Chat(Graphemeui parent){
		this.parent = parent;
		initWidget(uiBinder.createAndBindUi(this));
		mSendEnabled = false;
		output.setReadOnly(true);
		
		input.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent e) {
				if (input.getText().trim().length() > 0) {
					mSendEnabled = true;
					//notify other people that user is writing e.g:
				} else {
					mSendEnabled = false;
					//stop notifying e.g:
				}
				if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if(mSendEnabled){
						sendMessage();
					}
				}
			}
		});
		
		ClickHandler ch = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if(panel.isVisible()){
					panel.setVisible(false);
					minimize.setText("Show Chat");
				}else{
					panel.setVisible(true);
					minimize.setText("Hide Chat");
				}
			}
		};
		minimize.addClickHandler(ch);
	}

	public void sendMessage(){
		//do stuff here to send message
		ChatMessage cm = new ChatMessage("User" + mMyName, input.getText());
		ServerChannel.getInstance().send(cm.toJson());
		output.setText(output.getText() + "\nUser" +  mMyName + ":\n" + input.getText());
		input.setText("");
	}
	public static Chat getInstance(Graphemeui parent){
		if (sInstance == null) sInstance = new Chat(parent);
        return sInstance;
	}
	
	public static Chat getInstance() {
		return sInstance;
	}
	
	public void printDebugMessage(String message){
		output.setText(output.getText() + "\n" + message);
	}
	
	public void displayMessage(ChatMessage cm){
		output.setText(output.getText() + "\n" + cm.getUserId() + ":\n" + cm.getText());
	}
}
