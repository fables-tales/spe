package uk.me.graphe.client;

import uk.me.graphe.client.Chat.UiBinderChat;
import uk.me.graphe.client.communications.ServerChannel;
import uk.me.graphe.shared.messages.UserAuthMessage;
import uk.me.graphe.shared.messages.GraphListMessage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UserPanel extends Composite {
	private static UiBinderUserPanel uiBinder = GWT.create(UiBinderUserPanel.class);
	interface UiBinderUserPanel extends UiBinder<Widget, UserPanel> {}
	private static String mode = null;
	
    final static VerticalPanel pnlUser = new VerticalPanel();
		
	@UiField
	VerticalPanel mainPanel;
	@UiField
	HorizontalPanel secPanel;
	@UiField
	TextBox openIdUrl;
	@UiField
    static
	Button login;
	
	public static void requestGraphList(){
		GraphListMessage glm = new GraphListMessage();
		ServerChannel.getInstance().send(glm.toJson());
	}
	
	public static void displayGraphList(String list){
	    
	}
	
	public static void requestEmailAddress(final UserAuthMessage uam){
	      if(mode != "verify"){
	            return;
	        }
	        
	        final VerticalPanel pnlEmailRequest = new VerticalPanel();
	        final HorizontalPanel pnlEmail = new HorizontalPanel();
	        final TextBox emailAddress = new TextBox();
	        final Button submit = new Button("Submit");
	        
	        emailAddress.addKeyDownHandler(new KeyDownHandler()
	        {
	            @Override
	            public void onKeyDown(KeyDownEvent kc)
	            {
	                if (kc.getNativeKeyCode() == KeyCodes.KEY_ENTER)
	                {
	                    submit.click();
	                }
	            }
	        });
	        
	        
	        submit.addClickHandler(new ClickHandler() {
	    
	            public void onClick(ClickEvent event) {
	                if (submit.getText() == null || submit.getText().isEmpty()) {
	                    Window.alert("Please enter your email address.");
	                    return;
	                }
	                pnlEmailRequest.clear();
	                pnlEmailRequest.add(new HTML("Please wait..."));
	                uam.setEmailAddress(emailAddress.getText());
                    ServerChannel.getInstance().send(uam.toJson());
	            }
	        });
	        
	        RootPanel.get("canvas").clear();
	        
	        pnlEmail.add(emailAddress);
	        pnlEmail.add(submit);
	        pnlEmailRequest.add(new HTML("We couldn't get your e-mail address from your OpenID provider. Please enter it below:"));
	        pnlEmailRequest.add(pnlEmail);
	        
	        RootPanel.get("canvas").add(pnlEmailRequest);
	}
	
	public static void verify(){

		mode = "verify";
	    VerticalPanel pnlUser = new VerticalPanel();
	    pnlUser.setSize("400px", "350px");
	    pnlUser.setSpacing(5);
	    pnlUser.add(new HTML("<b>Verifying your OpenID. Please wait...</b>"));
	    RootPanel.get("canvas").add(pnlUser);
		
		Timer timer = new Timer() {
		    public void run() {
		    	UserAuthMessage uam = new UserAuthMessage();
		    	uam.setOpenIdUrl(Window.Location.getQueryString());
		    	uam.setAuthKey(Window.Location.getParameter("authKey"));
		    	uam.setId(Window.Location.getParameter("openid.identity"));
				ServerChannel.getInstance().send(uam.toJson());
			}
		}; 
		    timer.schedule(1000); 
	}
	
	public static void getEmailAddress(){
		
	    final TextBox openIdUrl = new TextBox();
	    final Button login = new Button("Sign in");
	    
		openIdUrl.addKeyDownHandler(new KeyDownHandler()
		{
			@Override
			public void onKeyDown(KeyDownEvent kc)
			{
				if (kc.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					login.click();
				}
			}
		});
	    
	    
	    login.addClickHandler(new ClickHandler() {
	
	        public void onClick(ClickEvent event) {
	            if (openIdUrl.getText() == null || openIdUrl.getText().isEmpty()) {
	            	Window.alert("Please enter your openIdUrl.");
	                return;
	            }
	            String url = openIdUrl.getText();
	            pnlUser.clear();
	        	pnlUser.add(new HTML("Please wait..."));
	        	UserAuthMessage uam = new UserAuthMessage(url);
				ServerChannel.getInstance().send(uam.toJson());
	        }
	    });
		
		
	}
	
	public static void show(){
		//show user panel
		HorizontalPanel outerPanel = new HorizontalPanel();
		outerPanel.add(new HTML("<div style=\"margin-top: 2em; margin-left: 3em;\">" +
				"</p> <h2 style=\"font-size: 2em; font-weight: bold; color: #7B7C7B; " +
				"margin-bottom: 0.5em;\">Welcome to grapheme</h2><p></p> <ul " +
				"style=\"margin-bottom: 2em; list-style-type: none;\"> <li> " +
				"<h4 style=\"font-size: 1.5em;" +
				" line-height: 1; height: 2em; padding-left: 45px; background-position:" +
				" 0 4px; background-repeat:no-repeat; color: #7B7C7B; background-image:" +
				" url(../images/ico1.png);\">Real-time collaboration</h4> <p>Grapheme " +
				"allows multi-user concurrent editing of graphs.</li> <li> <h4 style=\"font-size: " +
				"1.5em; line-height: 1; height:" +
				" 2em; padding-left: 45px; background-position: 0 4px; background-repeat:" +
				"no-repeat; color: #7B7C7B; background-image: url(../images/ico2.png);\">" +
				"Work Online or Offline</h4> <p>Your graphs can be viewed and edited offline " +
				"and your changes applied losslessly next time you have Internet access.</li> " +
				"<li> <h4 style=\"" +
				"font-size: 1.5em; line-height: 1; height: 2em; padding-left: 45px; " +
				"background-position: 0 4px; background-repeat:no-repeat; color: #7B7C7B;" +
				" background-image: url(../images/ico3.png);\">Do Things!</h4> <p>things." +
				" 3</li> </ul> </div> <p></p>", true));
	    final Label sign = new Label("Login with OpenID");
	    final TextBox openIdUrl = new TextBox();
	    final Button login = new Button("Sign in");
	    
		openIdUrl.addKeyDownHandler(new KeyDownHandler()
		{
			@Override
			public void onKeyDown(KeyDownEvent kc)
			{
				if (kc.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					login.click();
				}
			}
		});
	    
	    
	    login.addClickHandler(new ClickHandler() {
	
	        public void onClick(ClickEvent event) {
	            if (openIdUrl.getText() == null || openIdUrl.getText().isEmpty()) {
	            	Window.alert("Please enter your openIdUrl.");
	                return;
	            }
	            String url = openIdUrl.getText();
	            pnlUser.clear();
	        	pnlUser.add(new HTML("Please wait..."));
	        	UserAuthMessage uam = new UserAuthMessage(url);
				ServerChannel.getInstance().send(uam.toJson());
	        }
	    });
	
	    HorizontalPanel secPanel = new HorizontalPanel();
	    secPanel.add(openIdUrl);
	    secPanel.add(login);
	
	    Image google = new Image("images/google.png");
	    google.setSize("100px", "40px");
	    Image myspace = new Image("images/myspace.jpg");
	    myspace.setSize("100px", "40px");
	    Image yahoo = new Image("images/yahoo.jpg");
	    yahoo.setSize("100px", "40px");
	
	    FlowPanel fpanel = new FlowPanel();
	    fpanel.add(myspace);
	    fpanel.add(yahoo);
	
	    HorizontalPanel titlePanel = new HorizontalPanel();
	    titlePanel.add(sign);
	
	    pnlUser.setSpacing(7);
	    pnlUser.setSize("450px", "350px");
	    pnlUser.add(titlePanel);
	    pnlUser.add(secPanel);
	    pnlUser.add(new HTML("<b>Or choose from the following</b>"));
	    pnlUser.add(fpanel);
	    
	    outerPanel.add(pnlUser);
	
	    RootPanel.get("canvas").add(outerPanel);
		
	}

}
