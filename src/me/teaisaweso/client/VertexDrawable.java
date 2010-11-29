package me.teaisaweso.client;

public class VertexDrawable {
    private int mLeft;
    private int mTop;
    private int mWidth;
    private int mHeight;

    private String mLabel;

    /**
     * create a new VertexDrawable
     * 
     * @param left
     *            left of the bounding box
     * @param top
     *            top of the bounding box
     * @param width
     *            width of the bounding box
     * @param height
     *            height of the bounding box
     * @param label
     *            label of the vertex
     */
    public VertexDrawable(int left, int top, int width, int height, String label) {
        this.mLeft = left;
        this.mTop = top;
        this.mWidth = width;
        this.mHeight = height;
        this.mLabel = label;
    }

    /**
     * gets the left of the bounding box of the vertex
     * 
     * @return gets the left of the bounding box of the vertex
     */
    public int getLeft() {
        return mLeft;
    }

    /**
     * gets the top of the bounding box of the vertex
     * 
     * @return gets the top of the bounding box of the vertex
     */
    public int getTop() {
        return mTop;
    }

    /**
     * gets the width of the bounding box of the vertex
     * 
     * @return gets the width of the bounding box of the vertex
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * gets the height of the bounding box of the vertex
     * 
     * @return gets the height of the bounding box of the vertex
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * gets the string label of this vertex
     * 
     * @return a string representing the label of this vertex
     */
    public String getLabel() {
        return mLabel;
    }

    /**
     * updates the bounding rectangle of the drawable
     * 
     * @param left
     *            the left of the bounding rectangle
     * @param top
     *            the right of the bounding rectangle
     * @param width
     *            the width of the bounding rectangle
     * @param height
     *            the height of the bounding rectangle
     */
    public void updateBoundingRectangle(int left, int top, int width, int height) {
        mHeight = height;
        mWidth = width;
        mTop = top;
        mLeft = left;
    }
    
    /**
     * gets the center x of this vd
     * @return
     */
    public int getCenterX() {
        return mLeft + mWidth / 2;
    }
    
    /**
     * gets the center y of this vd
     * @return
     */
    public int getCenterY() {
        return mTop + mHeight / 2;
    }


}
