package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.Collection;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;
import com.googlecode.gwtgl.array.Float32Array;
import com.googlecode.gwtgl.array.Uint16Array;
import com.googlecode.gwtgl.binding.WebGLBuffer;
import com.googlecode.gwtgl.binding.WebGLCanvas;
import com.googlecode.gwtgl.binding.WebGLProgram;
import com.googlecode.gwtgl.binding.WebGLRenderingContext;
import com.googlecode.gwtgl.binding.WebGLShader;
import com.googlecode.gwtgl.binding.WebGLUniformLocation;
import uk.me.graphe.client.webglUtil.math.FloatMatrix4x4;
import uk.me.graphe.client.webglUtil.math.MatrixUtil;

public class DrawingImpl implements Drawing {

    // The level of zoom at which the edges begin to get thinner
    public static final double EDGE_ZOOM_LIMIT = 0.61;

    private WebGLRenderingContext mGlContext;
    private WebGLProgram mShaderProgram;
    private int mVertexPositionAttribute;
    private WebGLBuffer mVertexBuffer;

    private int mVertexColorAttribute;
    private FloatMatrix4x4 mProjectionMatrix;
    private WebGLBuffer mIndexBuffer;
    private WebGLBuffer mColorBuffer;
    private WebGLUniformLocation mProjectionMatrixUniform;

    private boolean mRenderRequest;
    private ArrayList<Float> mVerticesList = new ArrayList<Float>();
    private ArrayList<Integer> mIndicesList = new ArrayList<Integer>();
    private ArrayList<Float> mColorsList = new ArrayList<Float>();
    private ArrayList<Float> m2dList = new ArrayList<Float>();

    private Collection<EdgeDrawable> mEdgesToDraw;
    private Collection<VertexDrawable> mVerticesToDraw;
    private GWTCanvas m2dCanvas;

    private int mNumVertices = 0;
    private boolean mTimeOutSet = false;
    private boolean mCanRender = true;
    private int mFramesDone = 0;
    private int mCanvasWidth = 2000;
    private int mCanvasHeight = 2000; // Deliberate
    private boolean mWebglReady = false;

    private boolean mDo2d = false;
    private boolean mIsDigraph = true;
    private boolean mIsWeighted = true;
    private boolean mIsFlowChart = false;

    private double mLastRenderTime = 0;
    private DrawingPolygon mCurrentPolygon;

    WebGLCanvas webGLCanvas;

    // used for UI line
    private double[] mUIline = { 0, 0, 0, 0 };
    private boolean mShowUILine = false;

    // used for panning
    private int mOffsetX, mOffsetY;
    private double mZoom;

    // Edits the HTML to ensure 3d canvas is visible
    private static native int setUpCanvas()
    /*-{
		canvas1 = document.getElementsByTagName("canvas")[0];
		canvas1.style.position = "absolute";
    }-*/;

    public void toggle2d() {
        Console.log("Last render (2d:" + mDo2d + "):" + mLastRenderTime
                + " seconds.");
        if (mDo2d == false)
            mDo2d = true;
        else
            mDo2d = false;
    }

    // Does one time set up to make page ready for repeated rendering of graph
    private void setUpWebGL() {
        setUpCanvas();
        webGLCanvas = new WebGLCanvas(mCanvasWidth + "px", mCanvasHeight + "px");
        mGlContext = webGLCanvas.getGlContext();
        RootPanel.get("gwtGL").add(webGLCanvas);
        mGlContext.viewport(0, 0, mCanvasWidth, mCanvasHeight);
        initShaders();
        mGlContext.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mGlContext.clearDepth(1.0f);
        mGlContext.enable(WebGLRenderingContext.DEPTH_TEST);
        mGlContext.depthFunc(WebGLRenderingContext.LEQUAL);
        initProjectionMatrix();
        mWebglReady = true;
    }

    // set the coordinates of the UI line, and displays it
    public void setUILine(double startX, double startY, double endX, double endY) {
        mUIline[0] = startX;
        mUIline[1] = startY;
        mUIline[2] = endX;
        mUIline[3] = endY;
        mShowUILine = true;
    }

    // displays the UI line
    public void showUIline() {
        mShowUILine = true;
    }

    // hides the UI line
    public void hideUIline() {
        mShowUILine = false;
    }

    // check for WebGL
    private static native int webglCheck()/*-{
		if (typeof Float32Array != "undefined")
			return 1;
		return 0;
    }-*/;

    private void setNext(EdgeDrawable thisEdge) {
        double startX = (thisEdge.getStartX());
        double startY = (thisEdge.getStartY());
        double endX = (thisEdge.getEndX());
        double endY = (thisEdge.getEndY());
        for (EdgeDrawable thisEdge2 : mEdgesToDraw) {
            double startX2 = (thisEdge2.getStartX());
            double startY2 = (thisEdge2.getStartY());
            double endX2 = (thisEdge2.getEndX());
            double endY2 = (thisEdge2.getEndY());
            if ((startX == startX2 && startY == startY2 
                        && endX == endX2 && endY == endY2)
                    || (startX == endX2 && startY == endY2 
                            && endX == startX2 && endY == startY2)) {
                // Then this is the same edge
                thisEdge2.setDoubleFirst();

            }
        }
    }

    private void resetEdges() {
        for (EdgeDrawable thisEdge : mEdgesToDraw) {
            double startX = (thisEdge.getStartX());
            double startY = (thisEdge.getStartY());
            double endX = (thisEdge.getEndX());
            double endY = (thisEdge.getEndY());
            int count = 0;
            thisEdge.setDoubleSecond();
            thisEdge.unsetDouble();
            for (EdgeDrawable thisEdge2 : mEdgesToDraw) {
                double startX2 = (thisEdge2.getStartX());
                double startY2 = (thisEdge2.getStartY());
                double endX2 = (thisEdge2.getEndX());
                double endY2 = (thisEdge2.getEndY());
                if ((startX == startX2 && startY == startY2 && 
                            endX == endX2 && endY == endY2)
                        || (startX == endX2 && startY == endY2
                            && endX == startX2 && endY == startY2)) {
                    count++;

                }
            }
            if (count > 1) {
                thisEdge.setDouble();
            }
        }
    }

    private void doRendering() {

        // Update frames drawn
        mFramesDone++;
        // Can not render while render is happening
        mCanRender = false;

        // Do a (kind of reliable) check for webgl
        if (webglCheck() == 1 && mDo2d == false) {
            m2dCanvas.clear();
            // Can do WebGL
            mNumVertices = 0;
            String label = "";
            int vertexStyle;
            int edgeStyle;
            double edgeThickness = 6;
            float[] highlightColour = DrawingConstants.YELLOW;
            if (mZoom <= EDGE_ZOOM_LIMIT)
                edgeThickness = edgeThickness * mZoom;

            // Clear coordinates from last render
            mVerticesList.clear();
            mIndicesList.clear();
            mColorsList.clear();
            // SetupWebGl if not done so already
            if (!mWebglReady)
                setUpWebGL();

            resetEdges();
            // Loop through all edges and draw them
            for (EdgeDrawable thisEdge : mEdgesToDraw) {
                mCurrentPolygon = thisEdge.getPolygon();
                // Apply offset and zoom factors
                double startX = (thisEdge.getStartX() + mOffsetX) * mZoom;
                double startY = (thisEdge.getStartY() + mOffsetY) * mZoom;
                double endX = (thisEdge.getEndX() + mOffsetX) * mZoom;
                double endY = (thisEdge.getEndY() + mOffsetY) * mZoom;
                int weight = thisEdge.getWeight();
                float[] edgeColour = DrawingConstants.BLACK;
                float[] textColour = DrawingConstants.BLACK;

                int strokeThickness = 4;
                // edgeStyle = thisEdge.getStyle();
                edgeStyle = 100;
                // If edge is highlighted apply set it to negative
                if (thisEdge.isHilighted()) {
                    edgeStyle = -edgeStyle;
                    edgeColour = DrawingConstants.YELLOW;
                }

                int pos = 0;
                if (thisEdge.getDoubleFirst()) {
                    pos = 1;
                }

                if (thisEdge.getDouble() && !thisEdge.getDoubleFirst()) {
                    pos = 2;
                    setNext(thisEdge);
                }
                if (startX < endX && pos == 1 && thisEdge.needsToFromArrow())
                    pos = 2;
                else if (startX <= endX && pos == 2
                        && thisEdge.needsToFromArrow())
                    pos = 1;
                if (startX < endX && pos == 1)
                    pos = 2;
                else if (startX >= endX && pos == 2)
                    pos = 1;

                // Add edge to lists to be rendered
                if (thisEdge.needsToFromArrow()) {
                    if (thisEdge.isHilighted()) {
                        addEdge(startX, startY, endX, endY, edgeThickness,
                                true, true, (edgeThickness * 5),
                                (edgeThickness * 5), weight + "",
                                highlightColour, textColour, pos);
                    } else {
                        addEdge(startX, startY, endX, endY, edgeThickness,
                                true, true, (edgeThickness * 5),
                                (edgeThickness * 5), weight + "", edgeColour,
                                textColour, pos);
                    }
                } else if (thisEdge.needsToFromArrow()) {
                    if (thisEdge.isHilighted()) {
                        addEdge(endX, endY, startX, startY, edgeThickness,
                                true, true, (edgeThickness * 5),
                                (edgeThickness * 5), weight + "",
                                highlightColour, textColour, pos);
                    } else {
                        addEdge(endX, endY, startX, startY, edgeThickness,
                                true, true, (edgeThickness * 5),
                                (edgeThickness * 5), weight + "", edgeColour,
                                textColour, pos);
                    }
                } else {
                    if (thisEdge.isHilighted()) {
                        addEdge(startX, startY, endX, endY, edgeThickness,
                                true, true, (edgeThickness * 5),
                                (edgeThickness * 5), weight + "",
                                highlightColour, textColour, pos);
                    } else {
                        addEdge(startX, startY, endX, endY, edgeThickness,
                                true, true, (edgeThickness * 5),
                                (edgeThickness * 5), weight + "", edgeColour,
                                textColour, pos);
                    }

                }
            }

            // Add UI line if required
            if (mShowUILine) {
                addEdge((mUIline[0] + mOffsetX) * mZoom,
                        (mUIline[1] + mOffsetY) * mZoom,
                        (mUIline[2] + mOffsetX) * mZoom,
                        (mUIline[3] + mOffsetY) * mZoom, edgeThickness, true,
                        false, edgeThickness * 5, edgeThickness * 5, "",
                        DrawingConstants.GREY, DrawingConstants.BLACK, 0);
            }

            for (VertexDrawable thisVertex : mVerticesToDraw) {
                mNumVertices++;
                double centreX = (thisVertex.getCenterX() + mOffsetX) * mZoom;
                double centreY = (thisVertex.getCenterY() + mOffsetY) * mZoom;
                double width = (thisVertex.getWidth()) * mZoom;
                double height = (thisVertex.getHeight()) * mZoom;
                label = thisVertex.getLabel();
                vertexStyle = thisVertex.getStyle();
                float[] customColor = { 0, 0, 0, 1 };

                if (vertexStyle == VertexDrawable.STROKED_TERM_STYLE) {
                    if (thisVertex.isHilighted()) {
                        addTerm(centreX, centreY, width, height,
                                DrawingConstants.YELLOW);
                        addTerm(centreX, centreY, width - 4, height - 4,
                                DrawingConstants.GREY);
                    } else {
                        addTerm(centreX, centreY, width, height,
                                DrawingConstants.BLACK);
                        addTerm(centreX, centreY, width - 4, height - 4,
                                DrawingConstants.GREY);
                    }
                    addStringBox(centreX, centreY, width - height, height,
                            label, DrawingConstants.BLACK);
                } else if (vertexStyle == VertexDrawable.STROKED_SQUARE_STYLE) {
                    if (thisVertex.isHilighted()) {
                        addSquare(centreX, centreY, width, height,
                                DrawingConstants.YELLOW);
                        addSquare(centreX, centreY, width - 4, height - 4,
                                DrawingConstants.GREY);
                    } else {
                        addSquare(centreX, centreY, width, height,
                                DrawingConstants.BLACK);
                        addSquare(centreX, centreY, width - 4, height - 4,
                                DrawingConstants.GREY);
                    }
                    addStringBox(centreX, centreY, width, height, label,
                            DrawingConstants.BLACK);
                } else if (vertexStyle == VertexDrawable.STROKED_DIAMOND_STYLE) {
                    if (thisVertex.isHilighted()) {
                        addDiamondStroke(centreX, centreY, width, height,
                                DrawingConstants.GREY, 2,
                                DrawingConstants.YELLOW);
                    } else {
                        addDiamondStroke(centreX, centreY, width, height,
                                DrawingConstants.GREY, 2,
                                DrawingConstants.BLACK);

                    }
                    addStringCircle(centreX, centreY, width / 2, label,
                            DrawingConstants.BLACK);
                } else if (vertexStyle == VertexDrawable.COLORED_FILLED_CIRCLE) {
                    customColor = thisVertex.getColor();
                    addCircle(centreX, centreY, width, customColor);
                    break;
                } else {
                    if (thisVertex.isHilighted()) {
                        addCircle(centreX, centreY, width, highlightColour);
                        addCircle(centreX, centreY, width - 4, highlightColour);
                    } else {
                        addCircle(centreX, centreY, width,
                                DrawingConstants.BLACK);
                    }
                }

            }

            renderGraph();

        } else {
            // Can't do webGL so draw on 2d Canvas
            renderGraph2d(m2dCanvas, mEdgesToDraw, mVerticesToDraw);
        }

        mCanRender = true;
    }

    private static native String getUrlJsni()
    /*-{
		canvas = document.getElementsByTagName("canvas")[0];
		return canvas.toDataURL();
    }-*/;

    public String getUrl() {
        String url = getUrlJsni();
        return url;
    }

    public void renderGraph(GWTCanvas canvasNew,
            Collection<EdgeDrawable> edgesNew,
            Collection<VertexDrawable> verticesNew) {
        m2dCanvas = canvasNew;
        mEdgesToDraw = edgesNew;
        mVerticesToDraw = verticesNew;
        mRenderRequest = true;
        if (!mTimeOutSet) {
            mTimeOutSet = true;

            Timer t = new Timer() {
                private double startTimer;

                public void run() {
                    if (mCanRender && mRenderRequest) {
                        Console.log("start render");
                        startTimer = System.currentTimeMillis();
                        doRendering();
                        mLastRenderTime = 
                            (System.currentTimeMillis()-startTimer)/1000;
                        mRenderRequest = false;
                    }
                }
            };
            t.scheduleRepeating(100 / 6);
        }

    }

    private void initProjectionMatrix() {
        mProjectionMatrix = MatrixUtil.createPerspectiveMatrix(45, mCanvasWidth
                / mCanvasHeight, 0.1f, 100);
    }

    public void initShaders() {
        WebGLShader fragmentShader = getShader(
                WebGLRenderingContext.FRAGMENT_SHADER, DrawingShaders.INSTANCE
                        .fragmentShader().getText());
        WebGLShader vertexShader = getShader(
                WebGLRenderingContext.VERTEX_SHADER, DrawingShaders.INSTANCE
                        .vertexShader().getText());

        mShaderProgram = mGlContext.createProgram();
        mGlContext.attachShader(mShaderProgram, vertexShader);
        mGlContext.attachShader(mShaderProgram, fragmentShader);
        mGlContext.linkProgram(mShaderProgram);

        if (!mGlContext.getProgramParameterb(mShaderProgram,
                WebGLRenderingContext.LINK_STATUS)) {
            throw new RuntimeException("Could not initialise shaders");
        }

        mGlContext.useProgram(mShaderProgram);

        mVertexPositionAttribute = mGlContext.getAttribLocation(mShaderProgram,
                "vertexPosition");
        mVertexColorAttribute = mGlContext.getAttribLocation(mShaderProgram,
                "vertexColor");

        mProjectionMatrixUniform = mGlContext.getUniformLocation(
                mShaderProgram, "projectionMatrix");
    }

    private WebGLShader getShader(int type, String source) {
        WebGLShader shader = mGlContext.createShader(type);

        mGlContext.shaderSource(shader, source);
        mGlContext.compileShader(shader);

        if (!mGlContext.getShaderParameterb(shader,
                WebGLRenderingContext.COMPILE_STATUS)) {
            throw new RuntimeException(mGlContext.getShaderInfoLog(shader));
        }
        return shader;
    }

    private int verticesIndex() {
        if (mVerticesList.size() == 0)
            return 0;
        else
            return mVerticesList.size() / 2;
    }

    private double stringPixelLength(String string) {
        int code;
        double width = 0;
        for (int i = 0; i < string.length(); i++) {
            code = string.codePointAt(i) - 32;
            width += DrawingConstants.HERSHEY_FONT[code][1];
        }

        return width;
    }

    private ArrayList<String> getLines(String string, double width, double size) {
        String[] wordArray = string.split(" ");
        int numWords = wordArray.length;
        ArrayList<String> lineList = new ArrayList<String>();
        String[] lineArray = new String[100];
        int lineNum = 1;
        double lineWidth = 0;
        int wordNum = 0;
        double spaceWidth = stringPixelLength(" ") * size;
        lineArray[0] = "0";
        lineList.add("0");
        lineArray[lineNum] = "";
        lineList.add("");

        for (int i = 0; i < numWords; i++) {
            double charWidth = spaceWidth + stringPixelLength(wordArray[i])
                    * size;

            if (width <= lineWidth + charWidth && i > 0) {
                if (lineNum < numWords) {
                    lineNum++;
                    lineArray[lineNum] = "";
                    lineList.add("");
                    lineWidth = 0;
                    wordNum = 0;
                }
            }

            if (wordNum > 0 && i > 0) {

                wordArray[i] = " " + wordArray[i];
            }
            lineArray[lineNum] += wordArray[i];
            lineList.set(lineNum, lineList.get(lineNum) + "" + wordArray[i]);
            lineWidth += charWidth;
            wordNum++;
            if (stringPixelLength(wordArray[i]) > stringPixelLength(wordArray[Integer
                    .parseInt(lineArray[0])]))
                lineArray[0] = "" + i;
            if (stringPixelLength(wordArray[i]) > stringPixelLength(wordArray[Integer
                    .parseInt(lineList.get(0))]))
                lineList.set(0, "" + i);
        }

        return lineList;

    }

    private void addStringCircle(double left, double top, double width,
            String string, float[] color) {

        double rad = width;
        double dis = Math.sin(Math.PI / 4) * rad;
        addStringBox(left, top, dis, dis, string, color);
    }

    private void addStringBox(double left, double top, double width,
            double height, String string, float[] color) {
        double sizeInc = 0.05;
        double bufferX = 5;
        double bufferY = 0;
        width -= bufferX;
        height -= bufferY;
        boolean cont = true;
        double orginLength = stringPixelLength(string);
        double origSize = (width / orginLength);
        double size = origSize;
        double lineHeight = size * 35;
        ArrayList<String> lineList = getLines(string, width, size);
        double hOff;
        int count = 0;
        String[] wordArray = string.split(" ");
        double longestWidth = 0;
        while (cont) {
            longestWidth = stringPixelLength(wordArray[Integer
                    .parseInt(lineList.get(0))]) * size;
            double tempSize = size + sizeInc;
            lineList = getLines(string, width, tempSize);
            lineHeight = tempSize * 35;
            double vSpace = height - (lineHeight * (lineList.size() - 1));

            if (vSpace > lineHeight && width > longestWidth + 10) {
                size = tempSize;
            } else if (vSpace < -10 || width < longestWidth) {
                size = size * 0.5;
            } else {
                cont = false;
            }
            count++;
        }

        hOff = -((lineList.size() - 2) * lineHeight) / 2;

        for (int i = 1; i < lineList.size(); i++) {
            addString(left, top + hOff, lineList.get(i), color, size);
            hOff += lineHeight;
        }

    }

    private void addString(double left, double top, String string,
            float[] color, double size) {
        double offset = -((stringPixelLength(string) / 2) * size);
        for (int i = 0; i < string.length(); i++) {
            offset += addChar(left + offset, top, string.charAt(i) + "", color,
                    size);

        }
    }

    private double addChar(double left, double top, String character,
            float[] color, double size) {

        int code = character.codePointAt(0) - 32;
        int verticesNeeded = DrawingConstants.HERSHEY_FONT[code][0];
        double width = DrawingConstants.HERSHEY_FONT[code][1];
        double left1;
        double top1;
        double left2 = 0;
        double top2 = 0;
        int j = 0;
        int i = 2;
        double fHeight = 9 * size;
        double thickness = 1.5;

        while (i < (verticesNeeded * 2) + 2) {
            j++;
            left1 = DrawingConstants.HERSHEY_FONT[code][i] * size + left;
            top1 = (fHeight - DrawingConstants.HERSHEY_FONT[code][i + 1] * size)
                    + top;
            if (DrawingConstants.HERSHEY_FONT[code][i] != -1
                    && DrawingConstants.HERSHEY_FONT[code][i + 1] != -1) {
                if (i > 2 && DrawingConstants.HERSHEY_FONT[code][i - 1] != -1
                        && DrawingConstants.HERSHEY_FONT[code][i - 2] != -1) {
                    addEdge(left2, top2, left1, top1, thickness, false, false,
                            0, 0, "", color, color, 0);
                }
                if (DrawingConstants.HERSHEY_FONT[code][i + 2] != -1
                        && DrawingConstants.HERSHEY_FONT[code][i + 3] != -1) {
                    left2 = DrawingConstants.HERSHEY_FONT[code][i + 2] * size
                            + left;
                    top2 = (fHeight - DrawingConstants.HERSHEY_FONT[code][i + 3]
                            * size)
                            + top;
                    addEdge(left1, top1, left2, top2, thickness, false, false,
                            0, 0, "", color, color, 0);
                }
                i += 4;
            } else {
                i += 2;
            }
        }
        return width * size;
    }

    private void addTerm(double x, double y, double width, double height,
            float[] color) {
        double squareWidth = width - height;
        addSquare(x, y, squareWidth, height, color);
        addCircle(x - squareWidth / 2, y, height, color);
        addCircle(x + squareWidth / 2, y, height, color);

    }

    private void addTriangle(double centreX, double centreY, double width,
            double height, double angle, boolean addToPolygon, float[] color,
            int pos,double lineOffset) {

        int arrowOffset = (int)(height*0.65);
        int aOff = 0;
        
        if (pos == 1)
            aOff = arrowOffset;

        if (pos == 2){
            aOff = arrowOffset;
            lineOffset = -lineOffset;
        }

        int startIndex = verticesIndex();
        double halfHeight = (height / 2);
        double halfWidth = (width / 2);

        double[][] coords = { { 0, halfHeight }, { -halfWidth, -halfHeight },
                { halfWidth, -halfHeight } };

        double oldX;
        double oldY;

        for (int i = 0; i < coords.length; i++) {
            oldX = coords[i][0] + lineOffset;
            oldY = coords[i][1] + aOff;
            coords[i][0] = ((oldX) * Math.cos(angle))
                    - (oldY * Math.sin(angle));
            coords[i][1] = ((oldX) * Math.sin(angle))
                    + (oldY * Math.cos(angle));
        }

        mVerticesList.add((float) (coords[0][0] + centreX)); // topLeftX
        mVerticesList.add((float) (coords[0][1] + centreY));// topLeftY
        mVerticesList.add((float) (coords[1][0] + centreX)); // bottomLeftX
        mVerticesList.add((float) (coords[1][1] + centreY));// bottomLeftY
        mVerticesList.add((float) (coords[2][0] + centreX)); // bottomRightX
        mVerticesList.add((float) (coords[2][1] + centreY));// bottomRightY

        mIndicesList.add(startIndex + 0);
        mIndicesList.add(startIndex + 1);
        mIndicesList.add(startIndex + 2);

        Integer[] xArray = { (int) ((coords[0][0] + centreX)),
                (int) ((coords[2][0] + centreX)),
                (int) ((coords[1][0] + centreX)) };

        Integer[] yArray = { (int) ((coords[0][1] + centreY)),
                (int) ((coords[2][1] + centreY)),
                (int) ((coords[1][1] + centreY)) };

        if (addToPolygon)
            mCurrentPolygon.set(xArray, yArray, mZoom, mOffsetX, mOffsetY);

        for (int i = 0; i < 3; i++) {
            mColorsList.add(color[0]);
            mColorsList.add(color[1]);
            mColorsList.add(color[2]);
            mColorsList.add(color[3]);
        }
    }

    private void addCircle(double centreX, double centreY, double width,
            float[] color) {
        float radius = (float) (width / 2);
        float oldX;
        float newX;
        float newY;
        float movedX;
        float movedY;
        int numSegments = (int) ((int) 4 * Math.sqrt((double) radius));
        float theta = (float) (2 * 3.1415926 / numSegments);
        float cosValue = (float) Math.cos(theta);
        float sineValue = (float) Math.sin(theta);
        newY = 0;
        newX = (float) radius;
        int startIndex;
        int currentIndex;
        int oldIndex = 0;
        int circleStart = 0;
        if (mVerticesList.size() == 0)
            startIndex = 0;
        else
            startIndex = mVerticesList.size() / 2;
        mVerticesList.add((float) centreX);
        mVerticesList.add((float) centreY);
        for (int i = 0; i < numSegments; i++) {
            oldX = newX;
            newX = (cosValue * newX) - (sineValue * newY);
            newY = (sineValue * oldX) + (cosValue * newY);
            movedX = (float) (newX + centreX);
            movedY = (float) (newY + centreY);

            if (mVerticesList.size() == 0)
                currentIndex = 0;
            else
                currentIndex = mVerticesList.size() / 2;

            mVerticesList.add(movedX);
            mVerticesList.add(movedY);

            if (i == numSegments - 1) {
                mIndicesList.add(startIndex);
                mIndicesList.add(currentIndex);
                mIndicesList.add(currentIndex - 1);
                mIndicesList.add(startIndex);
                mIndicesList.add(currentIndex);
                mIndicesList.add(circleStart);
            } else if (oldIndex > 0) {

                mIndicesList.add(startIndex);
                mIndicesList.add(currentIndex);
                mIndicesList.add(currentIndex - 1);
            } else {
                circleStart = currentIndex;
            }

            if (mVerticesList.size() == 0)
                oldIndex = 0;
            else
                oldIndex = mVerticesList.size() / 2;
        }

        for (int i = 0; i < numSegments + 1; i++) {
            mColorsList.add(color[0]);
            mColorsList.add(color[1]);
            mColorsList.add(color[2]);
            mColorsList.add(color[3]);
        }

    }

    double diamondStrokeAlgorithm(double width, double height, double strokeSize) {
        double angle1 = Math.atan(height / width);
        double angle2 = Math.PI / 2 - angle1;
        double opp = strokeSize * Math.sin(angle2);
        double width1Sq = (strokeSize * strokeSize) - (opp * opp);
        double width1 = Math.sqrt(width1Sq);
        double width2 = opp / (Math.tan(angle1));
        double fWidth = (width1 + width2) * 2;
        return fWidth;
    }

    private void addDiamondStroke(double x, double y, double width,
            double height, float[] color, double strokeSize, float[] strokeColor) {

        double wOff = diamondStrokeAlgorithm(width / 2, height / 2, strokeSize);
        double hOff = diamondStrokeAlgorithm(height / 2, width / 2, strokeSize);

        addDiamond(x, y, width, height, strokeColor);
        addDiamond(x, y, width - wOff, height - hOff, color);
    }

    private void addDiamond(double x, double y, double width, double height,
            float[] color) {

        float halfWidth = (float) (width / 2);
        float halfHeight = (float) (height / 2);
        int startIndex = verticesIndex();

        mVerticesList.add((float) x - halfWidth);
        mVerticesList.add((float) y);
        mVerticesList.add((float) x);
        mVerticesList.add((float) y - halfHeight);
        mVerticesList.add((float) x + halfWidth);
        mVerticesList.add((float) y);
        mVerticesList.add((float) x);
        mVerticesList.add((float) y + halfHeight);

        mIndicesList.add(startIndex + 0);
        mIndicesList.add(startIndex + 1);
        mIndicesList.add(startIndex + 2);
        mIndicesList.add(startIndex + 0);
        mIndicesList.add(startIndex + 2);
        mIndicesList.add(startIndex + 3);

        for (int i = 0; i < 4; i++) {
            mColorsList.add(color[0]);
            mColorsList.add(color[1]);
            mColorsList.add(color[2]);
            mColorsList.add(color[3]);

        }
    }

    private void addEdge(double x1, double y1, double x2, double y2,
            double thickness, boolean arrow, boolean addToPolygon,
            double arrowThickness, double arrowHeight, String label,
            float[] color, float[] textColor, int pos) {
        double height = y2 - y1;
        double width = x2 - x1;
        double length = Math.sqrt((height * height) + (width * width));
        double halfThick = thickness / 2;
        double halfLength = length / 2;
        double xOffset = x1 + (width / 2);
        double yOffset = y1 + (height / 2);
        double oldX;
        double oldY;
        double lineAngle = Math.atan(height / width);
        double arrowAngle = lineAngle;

        double lineOffset = 0;
        double lineSeperation = thickness*1.2;

        double[] currentPolyEdgeCoords;
        boolean notCached = true;
        
        if (pos == 1)
            lineOffset = -lineSeperation;
        if (pos == 2)
            lineOffset = lineSeperation;
        
        double[][] coords = { { -halfLength, halfThick+lineOffset },
                { halfLength, halfThick+lineOffset}, { -halfLength, -halfThick+lineOffset },
                { halfLength, -halfThick+lineOffset } };
        
        if(addToPolygon){
            currentPolyEdgeCoords = mCurrentPolygon.getEdgeCoords();
        
            if(pos == 0 && addToPolygon && mZoom == mCurrentPolygon.getZoom() &&
                    mOffsetX == mCurrentPolygon.getOffsetX() &&
                    mOffsetY == mCurrentPolygon.getOffsetY() &&
                    currentPolyEdgeCoords[0] == x1 &&
                    currentPolyEdgeCoords[1] == y1 &&
                    currentPolyEdgeCoords[2] == x2 &&
                    currentPolyEdgeCoords[3] == y2 ){
                    
                // the polygon already has the coordinates we need
                // can use stored coordinates
                coords = mCurrentPolygon.getSquareCoords();
                notCached = false;
            }
        }
        
        if(notCached){
    
            for (int i = 0; i < coords.length; i++) {
                oldX = coords[i][0];
                oldY = coords[i][1];
                coords[i][0] = (oldX * Math.cos(lineAngle))
                        - (oldY * Math.sin(lineAngle));
                coords[i][1] = (oldX * Math.sin(lineAngle))
                        + (oldY * Math.cos(lineAngle));
            }
            if (addToPolygon) {
                mCurrentPolygon.setEdgeCoords(x1,y1,x2,y2);
                mCurrentPolygon.setSquareCoords(coords);
            }
            
        }

        addSquare(coords[0][0] + xOffset, coords[0][1] + yOffset, coords[1][0]
                + xOffset, coords[1][1] + yOffset, coords[2][0] + xOffset,
                coords[2][1] + yOffset, coords[3][0] + xOffset, coords[3][1]
                        + yOffset, color);

        Integer[] xArray = { (int) ((coords[0][0] + xOffset)),
                (int) ((coords[2][0] + xOffset)),
                (int) ((coords[3][0] + xOffset)),
                (int) ((coords[1][0] + xOffset)) };

        Integer[] yArray = { (int) ((coords[0][1] + yOffset)),
                (int) ((coords[2][1] + yOffset)),
                (int) ((coords[3][1] + yOffset)),
                (int) ((coords[1][1] + yOffset)) };

        if (addToPolygon) {
            mCurrentPolygon.clear();
            mCurrentPolygon.set(xArray, yArray, mZoom, mOffsetX, mOffsetY);
        }

        if (arrow && (mIsDigraph || mIsFlowChart)) {
            if (x1 > x2)
                arrowAngle -= Math.PI;
            addTriangle(xOffset, yOffset, arrowThickness, arrowHeight,
                    arrowAngle - Math.PI / 2, addToPolygon, color, pos, lineOffset);
        }

        if (!label.equals("") && mIsWeighted && !mIsFlowChart) {
            double lX = 0;
            double lY;
            double nlX;
            double nlY;
            double lLength= stringPixelLength(label);;
            double halfLLength = lLength / 2;
            double dLine = thickness * 2;
            double labelOffset = thickness*3;
            
            lY = -dLine;
            
            if (pos == 1)
                lX = -labelOffset;
            if (pos == 2)
                lX = labelOffset;
            if(pos != 0)
                lY  -= lineSeperation;

            
            nlX = (lX * Math.cos(lineAngle)) - (lY * Math.sin(lineAngle))
                    + xOffset;
            nlY = (lX * Math.sin(lineAngle)) + (lY * Math.cos(lineAngle))
                    + yOffset;

            if (x2 >= x1 && y1 < y2)
                nlX += halfLLength * 0.7;
            else if (x2 < x1 && y1 < y2)
                nlX -= halfLLength * 0.7;
            else if (x2 < x1 && y1 > y2)
                nlX += halfLLength * 0.7;
            else if (x2 >= x1 && y1 > y2)
                nlX -= halfLLength * 0.7;

            addString(nlX, nlY - (thickness * 1.6), label, textColor,
                    thickness * 0.1);
        }
    }

    private void addSquare(double x, double y, double width, double height,
            float[] color) {
        float halfWidth = (float) (width / 2);
        float halfHeight = (float) (height / 2);

        addSquare(x - halfWidth, y - halfHeight, x + halfWidth, y - halfHeight,
                x - halfWidth, y + halfHeight, x + halfWidth, y + halfHeight,
                color);

    }

    private void addSquare(double topLeftX, double topLeftY, double topRightX,
            double topRightY, double bottomLeftX, double bottomLeftY,
            double bottomRightX, double bottomRightY, float[] color) {

        int startIndex = verticesIndex();

        mVerticesList.add((float) topLeftX);
        mVerticesList.add((float) topLeftY);
        mVerticesList.add((float) bottomLeftX);
        mVerticesList.add((float) bottomLeftY);
        mVerticesList.add((float) topRightX);
        mVerticesList.add((float) topRightY);
        mVerticesList.add((float) bottomRightX);
        mVerticesList.add((float) bottomRightY);

        mIndicesList.add(startIndex + 0);
        mIndicesList.add(startIndex + 1);
        mIndicesList.add(startIndex + 2);
        mIndicesList.add(startIndex + 1);
        mIndicesList.add(startIndex + 2);
        mIndicesList.add(startIndex + 3);

        for (int i = 0; i < 4; i++) {
            mColorsList.add(color[0]);
            mColorsList.add(color[1]);
            mColorsList.add(color[2]);
            mColorsList.add(color[3]);
        }
    }

    private void renderGraph() {

        int vl = 0;
        float factor;
        float glWidth = 5f;
        float unit = 5f;

        float[] colors = new float[mColorsList.size()];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = mColorsList.get(i);
        }

        int[] indices = new int[mIndicesList.size()];
        for (int i = 0; i < mIndicesList.size(); i++) {
            Integer f = mIndicesList.get(i);
            indices[i] = f;
        }

        if (mCanvasWidth >= mCanvasHeight)
            factor = glWidth / mCanvasHeight;
        else
            factor = glWidth / mCanvasWidth;
        factor = factor * 2;
        float[] vertices2 = new float[(mVerticesList.size() / 2)
                + mVerticesList.size()];
        for (int i = 0; i < mVerticesList.size(); i++) {
            float current = (mVerticesList.get(i)) * factor;
            if (i % 2 != 0) {
                vertices2[vl] = -current + unit;
                vl++;
                vertices2[vl] = (-5.0f);
            } else {
                vertices2[vl] = (current) - unit;
            }
            vl++;
        }

        mGlContext.clear(WebGLRenderingContext.COLOR_BUFFER_BIT
                | WebGLRenderingContext.DEPTH_BUFFER_BIT);

        mVertexBuffer = mGlContext.createBuffer();
        mGlContext
                .bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mVertexBuffer);

        mGlContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                Float32Array.create(vertices2),
                WebGLRenderingContext.STATIC_DRAW);

        // create the colorBuffer
        mColorBuffer = mGlContext.createBuffer();
        mGlContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mColorBuffer);

        mGlContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER,
                Float32Array.create(colors), WebGLRenderingContext.STATIC_DRAW);

        // create the indexBuffer
        mIndexBuffer = mGlContext.createBuffer();
        mGlContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                mIndexBuffer);

        mGlContext.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                Uint16Array.create(indices), WebGLRenderingContext.STATIC_DRAW);

        mGlContext
                .bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mVertexBuffer);
        mGlContext.vertexAttribPointer(mVertexPositionAttribute, 3,
                WebGLRenderingContext.FLOAT, false, 0, 0);

        mGlContext.enableVertexAttribArray(mVertexPositionAttribute);

        mGlContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mColorBuffer);
        mGlContext.vertexAttribPointer(mVertexColorAttribute, 4,
                WebGLRenderingContext.FLOAT, false, 0, 0);
        mGlContext.enableVertexAttribArray(mVertexColorAttribute);

        mGlContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                mIndexBuffer);

        // Bind the projection matrix to the shader
        mGlContext.uniformMatrix4fv(mProjectionMatrixUniform, false,
                mProjectionMatrix.getColumnWiseFlatData());

        // Draw the polygon
        mGlContext.drawElements(WebGLRenderingContext.TRIANGLES,
                indices.length, WebGLRenderingContext.UNSIGNED_SHORT, 0);

        mGlContext.flush();

    }

    private float[] createPerspectiveMatrix(int fieldOfViewVertical,
            float aspectRatio, float minimumClearance, float maximumClearance) {

        float top = minimumClearance
                * (float) Math.tan(fieldOfViewVertical * Math.PI / 360.0);
        float bottom = -top;
        float left = bottom * aspectRatio;
        float right = top * aspectRatio;

        float X = 2 * minimumClearance / (right - left);
        float Y = 2 * minimumClearance / (top - bottom);
        float A = (right + left) / (right - left);
        float B = (top + bottom) / (top - bottom);
        float C = -(maximumClearance + minimumClearance)
                / (maximumClearance - minimumClearance);
        float D = -2 * maximumClearance * minimumClearance
                / (maximumClearance - minimumClearance);

        return new float[] { X, 0.0f, A, 0.0f, 0.0f, Y, B, 0.0f, 0.0f, 0.0f, C,
                -1.0f, 0.0f, 0.0f, D, 0.0f };
    };

    // old 2d canvas functions
    public void renderGraph2d(GWTCanvas canvas, Collection<EdgeDrawable> edges,
            Collection<VertexDrawable> vertices) {
        canvas.clear();
        canvas.setLineWidth(5);
        canvas.setStrokeStyle(Color.BLACK);
        canvas.setFillStyle(Color.BLACK);
        canvas.setFillStyle(Color.WHITE);
        canvas.fillRect(0, 0, 2000, 2000);
        canvas.setFillStyle(Color.BLACK);
        drawGraph(edges, vertices, canvas);

    }

    // Draws a single vertex, currently only draws circular nodes
    private void drawVertex(VertexDrawable vertex, GWTCanvas canvas) {
        double centreX = (vertex.getLeft() + 0.5 * vertex.getWidth() + mOffsetX)
                * mZoom;
        double centreY = (vertex.getTop() + 0.5 * vertex.getHeight() + mOffsetY)
                * mZoom;
        double radius = (vertex.getWidth() * 0.5) * mZoom;

        canvas.moveTo(centreX, centreY);
        canvas.beginPath();
        canvas.arc(centreX, centreY, radius, 0, 360, false);
        canvas.closePath();
        canvas.stroke();
        canvas.fill();

        // code that emulates caluclation needed if this 2d had all features
        for (int i = 0; i < 20; i++) {
            float oldX = (float) centreX;
            float oldY = (float) centreY;
            float angle = 0.23f;
            float coords1 = (float) ((float) (oldX) * Math.cos(angle))
                    - (float) (oldY * Math.sin(angle));
            float coords2 = (float) ((float) (oldX) * Math.sin(angle))
                    + (float) (oldY * Math.cos(angle));
            m2dList.add(coords1);
            m2dList.add(coords2);
        }

    }

    // Draws a line from coordinates to other coordinates
    private void drawEdge(EdgeDrawable edge, GWTCanvas canvas) {
        double startX = (edge.getStartX() + mOffsetX) * mZoom;
        double startY = (edge.getStartY() + mOffsetY) * mZoom;
        double endX = (edge.getEndX() + mOffsetX) * mZoom;
        double endY = (edge.getEndY() + mOffsetY) * mZoom;

        canvas.beginPath();
        canvas.moveTo(startX, startY);
        canvas.lineTo(endX, endY);
        canvas.closePath();
        canvas.stroke();

        // code that emulates caluclation needed if this 2d had all features
        for (int i = 0; i < 20; i++) {
            float oldX = (float) startX;
            float oldY = (float) startY;
            float angle = 0.23f;
            float coords1 = (float) ((float) (oldX) * Math.cos(angle))
                    - (float) (oldY * Math.sin(angle));
            float coords2 = (float) ((float) (oldX) * Math.sin(angle))
                    + (float) (oldY * Math.cos(angle));
            m2dList.add(coords1);
            m2dList.add(coords2);
        }

    }

    // Takes collections of edges and vertices and draws a graph on a specified
    // canvas.
    private void drawGraph(Collection<EdgeDrawable> edges,
            Collection<VertexDrawable> vertices, GWTCanvas canvas) {
        for (EdgeDrawable thisEdge : edges) {
            drawEdge(thisEdge, canvas);
        }
        for (VertexDrawable thisVertex : vertices) {
            drawVertex(thisVertex, canvas);
        }
    }

    // set offset in the event of a pan
    public void setOffset(int x, int y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    // getters for offsets
    public int getOffsetX() {
        return mOffsetX;
    }

    // set the mode
    public void setFlowChart(boolean mode) {
        mIsFlowChart = mode;
    }

    public int getOffsetY() {
        return mOffsetY;
    }

    // set zoom
    public void setZoom(double z) {
        mZoom = z;
    }

    public double getZoom() {
        return mZoom;
    }

    public void setIsDigraph(boolean isDigraph) {
        mIsDigraph = isDigraph;
    }

    public boolean isDigraph() {
        return mIsDigraph;
    }

    public void setIsWeighted(boolean isWeighted) {
        this.mIsWeighted = isWeighted;
    }

    public boolean isWeighted() {
        return this.mIsWeighted;
    }

    public void setIsFlowChart(boolean isFlowChart) {
        this.mIsFlowChart = isFlowChart;
    }

    public boolean isFlowChart() {
        return mIsFlowChart;
    }

    public double getRenderTime() {
        return mLastRenderTime;
    }
}
