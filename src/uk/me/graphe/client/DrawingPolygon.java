package uk.me.graphe.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;

public class DrawingPolygon {

    double mZoom = 1;
    private int mOffsetX, mOffsetY;
    private ArrayList<Integer[]> polygonXList = new ArrayList<Integer[]>();
    private ArrayList<Integer[]> polygonYList = new ArrayList<Integer[]>();
    
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
