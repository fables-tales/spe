package uk.me.graphe.client;

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
    private boolean mArrow;
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
        this(startX, startY, endX, endY, false);
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
     * @param needsArrow
     *            whether the end of the line needs an arrow
     */
    public EdgeDrawable(int startX, int startY, int endX, int endY, boolean needsArrow) {
        mStartX = startX;
        mStartY = startY;

        mEndX = endX;
        mEndY = endY;

        mArrow = needsArrow;
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
     * determines if the line needs an arrow at the end
     * 
     * @return true if the line needs an arrow at the end else false
     */
    public boolean needsArrow() {
        return mArrow;
    }
    
    
    public boolean isHilighted() {
        return mHilighted;
    }
    
    public void setHilighted(boolean h) {
        mHilighted = h;
    } 
}
