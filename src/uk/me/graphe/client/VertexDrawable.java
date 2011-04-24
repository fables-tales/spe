package uk.me.graphe.client;

public class VertexDrawable {
    private int mLeft;
    private int mTop;
    private int mWidth;
    private int mHeight;
    private int mStyle = UNDEFINED_STYLE;
    
    public static final int UNDEFINED_STYLE = -1;
    public static final int FILLED_CIRCLE_STYLE = 0x01;
    public static final int STROKED_TERM_STYLE = 0x02;
    public static final int STROKED_SQUARE_STYLE = 0x03;
    public static final int STROKED_DIAMOND_STYLE = 0x04;
    public static final int COLORED_FILLED_CIRCLE = 0x05;
    

    private String mLabel;
    private boolean mHilighted;
    private int[] mColor = new int[]{0,0,0};

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
    
    public VertexDrawable(int left, int top, int width, int height, String label, int style) {
        this(left, top, width, height, label);
        mStyle = style;
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
     * updates the bounding rectangle of the drawable
     * 
     * @param width
     *            the width of the bounding rectangle
     * @param height
     *            the height of the bounding rectangle
     */
    public void updateSize(int width, int height){
    	mLeft = getCenterX()-width/2;
    	mTop = getCenterY()-height/2;
    	mHeight = height;
    	mWidth = width;
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

    /**
     * determines if a vd contains a specific co-ordinate 
     * @param x
     * @param y
     * @return
     */
    public boolean contains(int x, int y) {
        return x >= mLeft && x <= mLeft + mWidth && y >= mTop && y <= mTop + mHeight;
    }
    
    public void setCoords(){
        
        
    }

    public void setStyle(int style) {
        mStyle = style;
    }

    public int getStyle() {
        return mStyle;
    }
    
    public boolean isHilighted() {
        return mHilighted;
    }
    
    public void setHilighted(boolean h) {
        mHilighted = h;
    }
    
    
    public int[] getColor() {
        int[] ret = new int[3];
        for (int i = 0; i < 3; i++) ret[i] = mColor[i];
        return mColor;
    }
    
    public void setColor(int[] color) {
        for (int i = 0; i < 3; i++) {
            mColor[i] = color[i];
        }
    }
}
