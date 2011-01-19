package uk.me.graphe.server;

public class HeartbeatManager {
    
    public void beatWhenPossible(Client c) {
        ClientMessageSender cms = ClientMessageSender.getInstance();
        try {
			cms.sendHeartbeat(c);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
    }
}
