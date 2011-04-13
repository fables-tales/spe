package uk.me.graphe.client;

import uk.me.graphe.shared.VertexDirection;

public class EdgeDrawable {
    private int mStartX;
    private int mStartY;

    private int mEndX;
    private int mEndY;

    /**
     * determines of the edge needs an arrow
     * 
     * arrows are always at the end edge
     */
    private VertexDirection mArrowDirection;
    private boolean mHilighted;

    /**
     * creates a new EdgeDrawable, with no arrow
     * 
     * @param startX
     *            the start x of the edge
     * @param startY
     *            the start y of the edge
     * @param endX
     *            the end x of the edge
     * @param endY
     *            the end y of the edge
     */
    public EdgeDrawable(int startX, int startY, int endX, int endY) {
        this(startX, startY, endX, endY, VertexDirection.both);
    }

    /**
     * creates a new EdgeDrawable
     * 
     * @param startX
     *            the start x of the edge
     * @param startY
     *            the start y of the edge
     * @param endX
     *            the end x of the edge
     * @param endY
     *            the end y of the edge
     * @param arrowDir
     *            the direction in which the arrow should be drawn
     */
    public EdgeDrawable(int startX, int startY, int endX, int endY, VertexDirection arrowDir) {
        mStartX = startX;
        mStartY = startY;

        mEndX = endX;
        mEndY = endY;

        mArrowDirection = arrowDir;
    }

    /**
     * gets the x of the start of the line
     * 
     * @return int representing x at the start of the line
     */
    public int getStartX() {
        return mStartX;
    }

    /**
     * gets the y of the start of the line
     * 
     * @return int representing y at the start of the line
     */
    public int getStartY() {
        return mStartY;
    }

    /**
     * gets the x of the end of the line
     * 
     * @return int represehnting x at the end of the line
     */
    public int getEndX() {
        return mEndX;
    }

    /**
     * gets the y at the end of the line
     * 
     * @return int representing y at the end of the line
     */
    public int getEndY() {
        return mEndY;
    }

    /**
     * determines if the line needs an arrow in the fromTo direction
     * 
     * @return true if the line needs an arrow in the fromTo direction
     */
    public boolean needsFromToArrow() {
        return mArrowDirection == VertexDirection.fromTo;
    }
    
    public boolean needsToFromArrow() {
        return mArrowDirection == VertexDirection.toFrom;
    }
    
    
    public boolean isHilighted() {
        return mHilighted;
    }
    
    public void setHilighted(boolean h) {
        mHilighted = h;
    } 
}
