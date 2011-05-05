package uk.me.graphe.client;

public class DrawingPolygon {

    int[] mXArray = {0};
    int[] mYArray = {0};
    
    /**
     * Set the polygon coordinates
     * @param newXArray
     * @param newYArray
     */
    public void set(int[] newXArray, int[] newYArray){
        mXArray = newXArray;
        mYArray = newYArray;
    }
    
    /**
     * Returns true if point is in polygon
     * @param x
     * @param y
     * @return true if point is in polygon
     */
    public boolean contains(int x, int y){
        return pointInPoly(mXArray, mYArray, x, y);
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
