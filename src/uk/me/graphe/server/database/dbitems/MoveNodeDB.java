package uk.me.graphe.server.database.dbitems;


import uk.me.graphe.shared.Vertex;

public class MoveNodeDB extends NodeDB {

    @Override
    public boolean movesNode(Vertex effectedNode) {
        return this.getNode().equals(effectedNode);
    }

    private int mToX;
    private int mToY;
    
    public MoveNodeDB(Vertex v, int toX, int toY) {
        super(v);
        mToX = toX;
        mToY = toY;
    }

    public int getToX() {
        return mToX;
    }
    
    public int getToY() {
        return mToY;
    }

}
