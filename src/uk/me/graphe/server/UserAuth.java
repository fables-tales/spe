package uk.me.graphe.server;

import java.util.List;
import java.security.SecureRandom;
import java.math.BigInteger;

import uk.me.graphe.server.UserAuthInfo;
import uk.me.graphe.shared.messages.UserAuthMessage;
import uk.me.graphe.server.DiscoveryStore;
import uk.me.graphe.server.DataManager;
import uk.me.graphe.server.database.UserDatabase;

import org.openid4java.OpenIDException;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.*;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.Identifier;
import org.openid4java.discovery.DiscoveryInformation;

import com.google.gwt.user.client.Window;


public class UserAuth {
	
	public static ConsumerManager manager;
	
    public UserAuth()
    {
        // instantiate the ConsumerManager object
    	try {
    		manager = new ConsumerManager();
    	} catch (ConsumerException e) {
    		//TODO: error handling
    	}
    }

	private static String authKeyGen(){
		  	  	SecureRandom random = new SecureRandom();
	    	    return new BigInteger(130, random).toString(32);
	}
	
	public UserAuthMessage authenticateOpenId(String opUrl) {
		
    	System.err.println("attempting op discovery for " + opUrl);
    	
    	//generate the authkey for a userauth transaction
    	String authKey = authKeyGen();
    	
	    try {
	        // return url for after authentication
	        String returnUrl = "http://127.0.0.1:1338/?action=userauth&authKey=" + authKey;
	        // endpoint discover
	        List discoveries = manager.discover(opUrl);
	        DiscoveryInformation discovered = manager.associate(discoveries);
	       
	        AuthRequest authReq = manager.authenticate(discovered, returnUrl);
	        FetchRequest fetch = FetchRequest.createFetchRequest();
	        // we also want the user's e-mail address
	        fetch.addAttribute("email","http://schema.openid.net/contact/email",true);
	        authReq.addExtension(fetch);
	        // create the UserAuthMessage object to be passed to the client
	        UserAuthMessage uam = new UserAuthMessage();
	        uam.setAuthKey(authKey);
	        
	        DiscoveryStore.addDisc(authKey, discovered);
	        
	        if(!discovered.isVersion2()){
	        	//TODO: this might be the root of google problem, keep for now
	            uam.setRedirectionUrl(authReq.getDestinationUrl(true));
	            //return to the client so we can do a JSNI redirect
	            return uam;
	            } else {
	            uam.setRedirectionUrl(authReq.getDestinationUrl(true));
	            //return to the client so we can do a JSNI redirect
	            return uam;
	        }
        } catch (MessageException ex) {
	        return null;
	    } catch (ConsumerException cex){
	    	return null;
	    } catch (DiscoveryException dex){
	    	return null;
	    }
	}
	
    /**
	* takes the parameters returned from the openID provider and verifies
	*
	* @param opUrl
	* the openID url
	* 
	* @returns UserAuthMessage containing link for redirection to
	* openID provider for authentication.
	*/
	public boolean verifyOpenId(UserAuthMessage uam){
		
    	System.err.println("attempting verification");
		
        try{
            // extract the parameters from the authentication response
            // (GET vars from the url)
        	String qs = uam.getOpenIdUrl().substring(52); //this removes the extra vars we added
            ParameterList response = ParameterList.createFromQueryString(qs);

            // retrieve the previously stored discovery information
            DiscoveryInformation discovered = DiscoveryStore.getDisc(uam.getAuthKey());
            
            System.err.println("discinfo string: " + discovered.toString());

            // this needs to be the url the provider redirected back to
            String receivingUrl = "http://127.0.0.1:1338/" + uam.getOpenIdUrl();
            
            // needs to be the same instance of ConsumerManager used for auth
            VerificationResult verification = manager.verify(
                    receivingUrl, response, discovered);

            // check verification and extract verified identifier
            Identifier verified = verification.getVerifiedId();
            if (verified != null){
                AuthSuccess authSuccess =
                        (AuthSuccess) verification.getAuthResponse();

                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)){
                    FetchResponse fetchResp = (FetchResponse) authSuccess
                            .getExtension(AxMessage.OPENID_NS_AX);

                    List emails = fetchResp.getAttributeValues("email");
                    String email = (String) emails.get(0);
                }
                
                return true;  // success
                //TODO: add user to db (if new) and set client user id
                
            }
        }catch (OpenIDException e){
        	return false;
        }catch (NullPointerException e){
        	return false;
        }

        return false;
    }
	//*/
}
