package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;

@Embedded("graph")
public class MoveNodeDB extends NodeDB {

    public MoveNodeDB() {
        
    }
    @Override
    public boolean movesNode(DBVertex effectedNode) {
        return this.getNode().equals(effectedNode);
    }

    private int mToX;
    private int mToY;
    
    public MoveNodeDB(DBVertex storeVertex, int toX, int toY) {
        super(storeVertex);
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
