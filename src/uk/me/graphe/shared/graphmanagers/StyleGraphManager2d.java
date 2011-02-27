package uk.me.graphe.shared.graphmanagers;

public interface StyleGraphManager2d extends GraphManager2d {
    public void setVertexStyle(String label, int style);
    
    public int getVertexStyle(String label);
    
}
