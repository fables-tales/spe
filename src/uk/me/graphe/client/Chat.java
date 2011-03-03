package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.Random;

import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.shared.messages.ChatMessage;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Chat extends Composite{
	private static UiBinderChat uiBinder = GWT.create(UiBinderChat.class);
	interface UiBinderChat extends UiBinder<Widget, Chat> {}
	
	private static int mMyName = new Random().nextInt(10000);	
	private static Chat sInstance;
	
	private ArrayList<String> usersWriting;

	@UiField
	VerticalPanel pnlChat;
	@UiField
	HTML txtConvo;
	@UiField
	TextArea txtWrite;
	@UiField
	Button btnChat, btnGoOffline, btnClearChat;
	
	private final Graphemeui parent;
	private final Sound notifySound = new SoundController().createSound("audio/mpeg", "sounds/chatnotify.mp3", true);
	
	private int unreadCount;
	private boolean isOnline, isWriting;
	private String lastUserMessage;
	
	public Chat(Graphemeui gui){
		this.parent = gui;
		initWidget(uiBinder.createAndBindUi(this));
		
		btnChat.addStyleName("btnChatOnline");
		btnChat.addStyleName("btnChatOffline");
		btnChat.addStyleName("btnChatWriting");

		usersWriting = new ArrayList<String>();
		unreadCount = 0;
		isOnline = true;
		isWriting = false;
		lastUserMessage = "";
		
		pnlChat.setVisible(false);
		
		setButtonStyle();
		
		txtWrite.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent e) {
				if (txtWrite.getText().trim().length() > 0) {
					if (!isWriting) {
						ChatMessage cmNotify = new ChatMessage(String.valueOf(mMyName), null, true, true);
						ServerChannel.getInstance().send(cmNotify.toJson());
						isWriting = true;
					}
					
					if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER && !e.isShiftKeyDown()) {
						e.preventDefault();
						// TODO: Convert message to HTML entities.
						ChatMessage cmMessage = new ChatMessage(String.valueOf(mMyName), txtWrite.getText(), false, false);
						ServerChannel.getInstance().send(cmMessage.toJson());
						displayMessage(String.valueOf(mMyName), txtWrite.getText());
						txtWrite.setText("");
						isWriting = false;
					}
				} else if (isWriting) {
					ChatMessage cmNotify = new ChatMessage(String.valueOf(mMyName), null, true, false);
					ServerChannel.getInstance().send(cmNotify.toJson());
					isWriting = false;
				}
			}
		});
		
		btnChat.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if (pnlChat.isVisible()) {
					pnlChat.setVisible(false);
				} else {
					isOnline = true;
					resetUnreadCount();					
					pnlChat.setVisible(true);
					txtConvo.getElement().setScrollTop(txtConvo.getElement().getScrollHeight());
					txtWrite.setFocus(true);
				}
			}
		});
		
		btnClearChat.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				txtConvo.setHTML("");
			}
		});
		
		btnGoOffline.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				isOnline = false;
				//TODO: send something to the other users to let them know you are offline
				setButtonStyle();
				pnlChat.setVisible(false);
				txtConvo.setHTML("");
				if (isWriting)
				{
					ChatMessage cmNotify = new ChatMessage(String.valueOf(mMyName), null, true, false);
					ServerChannel.getInstance().send(cmNotify.toJson());
					isWriting = false;
				}
			}
		});
	}

	public static Chat getInstance(Graphemeui parent){
		if (sInstance == null) sInstance = new Chat(parent);
        return sInstance;
	}
	
	public static Chat getInstance() {
		return sInstance;
	}
	
	public void onReceiveMessage(ChatMessage cm) {
		if (isOnline) {
			if (cm.isNotification()) {
				if (cm.isUserWriting()) {
					addUserWriting(cm.getUserId());
				} else {
					removeUserWriting(cm.getUserId());
				}
			} else {
				removeUserWriting(cm.getUserId());
				displayMessage(cm.getUserId(), escapeHtml(cm.getText()));
			}
		}
	}
	
	private void displayMessage(String user, String message){		
		if (txtConvo.getHTML().length() == 0) {
			txtConvo.setHTML("<strong>" + user + ":</strong> " + message);
		} else if (lastUserMessage.compareTo(String.valueOf(user)) != 0) {
			txtConvo.setHTML(txtConvo.getHTML() + "<hr />" + "<strong>" + user + 
					":</strong> " + message);			
		} else {
			txtConvo.setHTML(txtConvo.getHTML() + "<br />" + message);	
		}
		
		lastUserMessage = user;
	
		if (!pnlChat.isVisible() && (user.compareTo(String.valueOf(mMyName)) != 0)) {
			incrementUnreadCount();
			notifySound.play();
		}
		
		txtConvo.getElement().setScrollTop(txtConvo.getElement().getScrollHeight());	
	}
	
	private void addUserWriting(String user) {
		usersWriting.add(user);
		setButtonStyle();
	}

	
	private void incrementUnreadCount() {
		unreadCount++;
		setButtonStyle();
		notifySound.play();
	}
		
	private void removeUserWriting(String user) {
		usersWriting.remove(user);
		setButtonStyle();
	}
	
	private void resetUnreadCount() {
		unreadCount = 0;
		setButtonStyle();
	}
	
	private String escapeHtml(String html){
		SimpleHtmlSanitizer shs = SimpleHtmlSanitizer.getInstance();
		html = shs.sanitize(html).asString();
		return html;
	}
	
	private void setButtonStyle() {
		if (isOnline){
			if (usersWriting.size() > 0) {
				btnChat.setStyleName("btnChatWriting");
			} else {
				btnChat.setStyleName("btnChatOnline");
			}
			
			if (unreadCount > 0) {
				btnChat.setText("Chat: " + String.valueOf(unreadCount) + " unread message(s)");
			} else {
				btnChat.setText("Chat: online");
			}
		} else {
			btnChat.setText("Chat: offline");
			btnChat.setStyleName("btnChatOffline");
		}
	}
}
