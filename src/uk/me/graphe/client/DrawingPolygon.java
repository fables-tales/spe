package uk.me.graphe.client;

import com.google.gwt.user.client.Window;

public class DrawingPolygon {

    int[] mXArray = {0};
    int[] mYArray = {0};
    double mZoom = 1;
    private int mOffsetX, mOffsetY;
    
    /**
     * Set the polygon coordinates
     * @param newXArray
     * @param newYArray
     */
    public void set(int[] newXArray, int[] newYArray, double newZoom, int newOffsetX, int newOffsetY){
        mXArray = newXArray;
        mYArray = newYArray;
        mOffsetX = newOffsetX;
        mOffsetY = newOffsetY;
        mZoom = newZoom;
    }
    
    /**
     * Returns true if point is in polygon
     * @param x
     * @param y
     * @return true if point is in polygon
     */
    public boolean contains(int x, int y){
        x = (int) ((x+mOffsetX)*mZoom);
        y = (int) ((y+mOffsetY)*mZoom);
        Boolean cont = pointInPoly(mXArray, mYArray, x, y);
        return cont;
    }
    
    private boolean pointInPoly(int X[], int Y[], int x, int y){
        int i, j;
        boolean c = false;
        for (i = 0, j = X.length-1; i < X.length; j = i++){
            if (( ((Y[i]<=y) && (y<Y[j])) || ((Y[j]<=y) && (y<Y[i])) ) &&
            (x < (X[j]-X[i]) * (y-Y[i]) / (Y[j]-Y[i]) + X[i]))
                c = !c;
        }
        return c;
    }
     
}
