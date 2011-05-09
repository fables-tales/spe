package uk.me.graphe.client;

import java.util.ArrayList;

public class DrawingPolygon {

    double mZoom = 1;
    private int mOffsetX, mOffsetY = 0;
    private double[] mEdgeCoords = new double[4];

    private ArrayList<Integer[]> polygonXList = new ArrayList<Integer[]>();
    private ArrayList<Integer[]> polygonYList = new ArrayList<Integer[]>();
    private double[][] mSquareCoords;
    
    /**
     * Set the coordinates if this polygon represents an edge
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void setEdgeCoords(double startX, double startY, double endX, double endY){
        mEdgeCoords[0] = startX;
        mEdgeCoords[1] = startY;
        mEdgeCoords[2] = endX;
        mEdgeCoords[3] = endY;
    }
    
    public double getZoom(){
        return mZoom;
    }
    
    public int getOffsetX(){
        return mOffsetX;
    }
    
    public int getOffsetY(){
        return mOffsetY;
    }
    
    public double[] getEdgeCoords(){
        return mEdgeCoords;
    }
    
    public void setSquareCoords(double[][] squareCoords){
        mSquareCoords = squareCoords;
    }

    public double[][] getSquareCoords(){
        return mSquareCoords;
    }
    
    
    /**
     * Set the polygon coordinates
     * @param newXArray
     * @param newYArray
     */
    public void set(Integer[] newXArray, Integer[] newYArray, double newZoom, int newOffsetX, int newOffsetY){
        polygonXList.add(newXArray);
        polygonYList.add(newYArray);
        mOffsetX = newOffsetX;
        mOffsetY = newOffsetY;
        mZoom = newZoom;
    }
    
    /**
     * Resets internal polygon coordinates
     */
    public void clear(){
        if(polygonXList.size()>0){
            polygonXList.clear();
            polygonYList.clear();
        }
    }
    
    /**
     * Returns true if point is in polygon
     * @param x
     * @param y
     * @return true if point is in polygon
     */
    public boolean contains(int x, int y){
        Boolean cont;
        x = (int) ((x+mOffsetX)*mZoom);
        y = (int) ((y+mOffsetY)*mZoom);
        Integer[] cXArray;
        Integer[] cYArray;
        for(int i = 0;i<polygonXList.size();i++){
            cXArray=polygonXList.get(i);
            cYArray=polygonYList.get(i);
            cont = pointInPoly(cXArray, cYArray, x, y);
            if(cont == true) return true;
        }
        return false;
    }
    
    private boolean pointInPoly(Integer X[], Integer Y[], Integer x, Integer y){
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
