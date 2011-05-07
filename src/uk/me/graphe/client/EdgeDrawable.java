package uk.me.graphe.client;

import uk.me.graphe.shared.VertexDirection;

public class EdgeDrawable {
    private int mStartX;
    private int mStartY;

    private int mEndX;
    private int mEndY;
    
    private boolean mDouble = false;
    private boolean mDoubleFirst = false;
    
    private DrawingPolygon mPolygon;
    
    /**
     * determines of the edge needs an arrow
     * 
     * arrows are always at the end edge
     */
    private VertexDirection mArrowDirection;
    private boolean mHilighted;
    private int mWeight;

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
    public EdgeDrawable(int startX, int startY, int endX, int endY, int weight) {
        this(startX, startY, endX, endY, weight, VertexDirection.both);
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
    public EdgeDrawable(int startX, int startY, int endX, int endY, int weight, VertexDirection arrowDir) {
        mStartX = startX;
        mStartY = startY;

        mEndX = endX;
        mEndY = endY;
        mWeight = weight;
        mArrowDirection = arrowDir;
        
        mPolygon = new DrawingPolygon();
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
     * determines if a ed contains a specific co-ordinate 
     * @param x
     * @param y
     * @return true if x and y in edge
     */
    public boolean contains(int x, int y) {
        return mPolygon.contains(x, y);
    }
    
    /**
     * Returns the polygon of the edge
     * @return the polygon of edge
     */
    public DrawingPolygon getPolygon(){
        return mPolygon;
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
    
    public int getWeight() {
        return mWeight;
    }
    
    public boolean isHilighted() {
        return mHilighted;
    }
    
    public void setHilighted(boolean h) {
        mHilighted = h;
    }
    
    public void setStartX(int x) {
        mStartX = x;
    }

    public void setStartY(int y) {
        mStartY = y;
    }

    public void setEndX(int x) {
        mEndX = x;
    }
    
    public void setEndY(int y) {
        mEndY = y;
    }
    
    public void setDouble(){
        mDouble = true;
    }
    
    public void unsetDouble(){
        mDouble = false;
    }
    
    public boolean getDouble(){
        return mDouble;
    }
    
    public void setDoubleFirst(){
        mDoubleFirst = true;
    }
    
    public boolean getDoubleFirst(){
        return mDoubleFirst;
    }
    
    public void setDoubleSecond(){
        mDoubleFirst = false;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }
    
}
