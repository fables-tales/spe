package uk.me.graphe.shared.jsonwrapper;

public class JSONException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public JSONException(Throwable e) {
        this.initCause(e);
    }

}
