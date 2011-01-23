package uk.me.graphe.server.database.dbitems;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public abstract class GraphDB {

    /**
     * the history id of the graph to apply the operation against
     */
    private int mApplyOn;

    public void setHistoryId(int i) {
        mApplyOn = i;
    }

    public int getHistoryId() {
        return mApplyOn;
    }

    public boolean isEdgeOperation() {
        return false;
    }

    public boolean isNodeOperation() {
        return false;
    }

    public NodeDB asNodeOperation() {
        return null;
    }

    public EdgeDB asEdgeOperation() {
        return null;
    }

    public void setApplyOn(int id) {
        mApplyOn = id;
    }

    public boolean isNoOperation() {
        return false;
    }

    public boolean isOperation() {
        return true;
    }

}
