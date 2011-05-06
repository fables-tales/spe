package uk.me.graphe.shared.graphmanagers;


public class OTStyleGraphManager2d extends OTGraphManager2dImpl implements StyleGraphManager2d {

    

    public OTStyleGraphManager2d(int id) {
        super(id);
    }

    @Override
    public void setVertexStyle(String label, int style) {
        this.getVertexDrawable(label).setStyle(style);
    }

    @Override
    public int getVertexStyle(String label) {
        return this.getVertexDrawable(label).getStyle();
    }

}
