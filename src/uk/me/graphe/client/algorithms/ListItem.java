package uk.me.graphe.client.algorithms;


public class ListItem {
    private String head;
    private String tail;
    private int weight = 0;
    
    public ListItem (String h, String t, int w) {
        head = h;
        tail = t;
        weight = w;
    }
    
    public String getHead () {
        return head;
    }
    
    public String getTail () {
        return tail;
    }
    
    public int getWeight () {
        return weight;
    }
}
