package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.Collection;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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

    private Collection<EdgeDrawable> mEdgesToDraw;
    private Collection<VertexDrawable> mVerticesToDraw;
    private GWTCanvas m2dCanvas;

    private int mNumVertices = 0;
    private boolean mTimeOutSet = false;
    private boolean mCanRender = true;
    private long mCurrentTime = -1;
    private long mOldTime = -1;
    private int mFramesDone = 0;
    private int mCanvasWidth = 2000;
    private int mCanvasHeight = 2000;
    private boolean mWebglReady = false;

    // used for styles
    private boolean mIsFlowChart;

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

    // Does one time set up to make page ready for repeated rendering of graph
    private void setUpWebGL() {
        setUpCanvas();
        final WebGLCanvas webGLCanvas = new WebGLCanvas(mCanvasWidth + "px", mCanvasHeight + "px");
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
        if (typeof Float32Array != "undefined")return 1;
        return 0;
    }-*/;

    private void doRendering() {

        // Update frames drawn
        mFramesDone++;
        // Can not render while render is happening
        mCanRender = false;

        // Do a (kind of reliable) check for webgl
        if (webglCheck() == 1) {
            mNumVertices = 0;
            String label = "";
            int vertexStyle;
            int edgeStyle;
            // Can do WebGL
            // Clear coordinates from last render
            mVerticesList.clear();
            mIndicesList.clear();
            mColorsList.clear();
            // SetupWebGl if not done so already
            if (!mWebglReady)
                setUpWebGL();

            // Loop through all edges and draw them
            for (EdgeDrawable thisEdge : mEdgesToDraw) {
                // Apply offset and zoom factors
                double startX = (thisEdge.getStartX() + mOffsetX) * mZoom;
                double startY = (thisEdge.getStartY() + mOffsetY) * mZoom;
                double endX = (thisEdge.getEndX() + mOffsetX) * mZoom;
                double endY = (thisEdge.getEndY() + mOffsetY) * mZoom;
                // edgeStyle = thisEdge.getStyle();
                edgeStyle = 100;
                // If edge is highlighted apply set it to negative
                if (thisEdge.isHilighted())
                    edgeStyle = -edgeStyle;
                // Add edge to lists to be rendered
                addEdge(startX, startY, endX, endY, 5, true, DrawingConstants.BLACK);
            }

            // Add UI line if required
            if (mShowUILine) {
                addEdge((mUIline[0]+ mOffsetX)*mZoom, (mUIline[1]+ mOffsetY)*mZoom, 
                        (mUIline[2]+ mOffsetX)*mZoom, (mUIline[3]+ mOffsetY)*mZoom,
                         5, true, DrawingConstants.GREY);
            }
            
            for (VertexDrawable thisVertex : mVerticesToDraw) {
                mNumVertices++;
                double centreX = (thisVertex.getCenterX() + mOffsetX) * mZoom;
                double centreY = (thisVertex.getCenterY() + mOffsetY) * mZoom;
                double width = (thisVertex.getWidth()) * mZoom;
                double height = (thisVertex.getHeight()) * mZoom;
                vertexStyle = thisVertex.getStyle();
                if (mIsFlowChart) {
                    switch (vertexStyle) {
                    case VertexDrawable.STROKED_TERM_STYLE:
                        vertexStyle = 100;
                        break;
                    case VertexDrawable.STROKED_SQUARE_STYLE:
                        vertexStyle = 101;
                        break;
                    case VertexDrawable.STROKED_DIAMOND_STYLE:
                        vertexStyle = 102;
                        break;
                    default:
                        vertexStyle = 1;
                        break;
                    }
                } else {
                    vertexStyle = 1;
                }
                label = thisVertex.getLabel();
                int[] customColor = { 0, 0, 0 };
                if (thisVertex.getStyle() == VertexDrawable.COLORED_FILLED_CIRCLE) {
                    vertexStyle = 5;
                    customColor = thisVertex.getColor();
                }
                if (thisVertex.isHilighted()) {
                    addCircle(centreX, centreY, width, DrawingConstants.BLACK);
                    addCircle(centreX, centreY, width - 4, DrawingConstants.YELLOW);
                } else {

                    addCircle(centreX, centreY, width, DrawingConstants.BLACK);
                }
                // addVertice(centreX,centreY,width,height,vertexStyle,label,
                // customColor[0],customColor[1],customColor[2]);
            }

            renderGraph();

            mCanRender = true;

        } else {
            // Cant do webGL so draw on 2d Canvas

            renderGraph2d(m2dCanvas, mEdgesToDraw, mVerticesToDraw);
        }

    }

    public void renderGraph(GWTCanvas canvasNew, Collection<EdgeDrawable> edgesNew,
            Collection<VertexDrawable> verticesNew) {

        m2dCanvas = canvasNew;
        mEdgesToDraw = edgesNew;
        mVerticesToDraw = verticesNew;
        mRenderRequest = true;
        if (!mTimeOutSet) {
            mTimeOutSet = true;
            mOldTime = System.currentTimeMillis();
            Timer t = new Timer() {
                public void run() {
                    if (mCanRender && mRenderRequest) {

                        doRendering();
                        mCurrentTime = System.currentTimeMillis();
                        if (mFramesDone % 50 == 0) {
                            float fps =
                                    1 /(((float)(System.currentTimeMillis()-mOldTime))/(float)1000);
                            fps = (float) (Math.round(((double) fps) * 100.0) / 100.0);
                            RootPanel.get("frameRate").clear();
                            VerticalPanel panel = new VerticalPanel();
                            HTML gLabel =
                                    new HTML("TotalFrames:" + mFramesDone + " Nodes:"
                                            + mNumVertices + " FPS:" + fps);
                            gLabel.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
                            panel.add(gLabel);
                            RootPanel.get("frameRate").add(panel);
                        }
                        mOldTime = System.currentTimeMillis();
                        mRenderRequest = false;
                    }
                }
            };

            t.scheduleRepeating(100 / 6);
        }

    }

    private void initProjectionMatrix() {
        mProjectionMatrix =
                MatrixUtil.createPerspectiveMatrix(45, mCanvasWidth / mCanvasHeight, 0.1f, 100);
    }

    public void initShaders() {
        WebGLShader fragmentShader =
                getShader(WebGLRenderingContext.FRAGMENT_SHADER, DrawingShaders.INSTANCE
                        .fragmentShader().getText());
        WebGLShader vertexShader =
                getShader(WebGLRenderingContext.VERTEX_SHADER, DrawingShaders.INSTANCE
                        .vertexShader().getText());

        mShaderProgram = mGlContext.createProgram();
        mGlContext.attachShader(mShaderProgram, vertexShader);
        mGlContext.attachShader(mShaderProgram, fragmentShader);
        mGlContext.linkProgram(mShaderProgram);

        if (!mGlContext.getProgramParameterb(mShaderProgram, WebGLRenderingContext.LINK_STATUS)) {
            throw new RuntimeException("Could not initialise shaders");
        }

        mGlContext.useProgram(mShaderProgram);

        mVertexPositionAttribute = mGlContext.getAttribLocation(mShaderProgram, "vertexPosition");
        mVertexColorAttribute = mGlContext.getAttribLocation(mShaderProgram, "vertexColor");

        mProjectionMatrixUniform =
                mGlContext.getUniformLocation(mShaderProgram, "projectionMatrix");
    }

    private WebGLShader getShader(int type, String source) {
        WebGLShader shader = mGlContext.createShader(type);

        mGlContext.shaderSource(shader, source);
        mGlContext.compileShader(shader);

        if (!mGlContext.getShaderParameterb(shader, WebGLRenderingContext.COMPILE_STATUS)) {
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

    private void addTriangle(double centreX, double centreY, double width, double height,
            double angle, float[] color) {

        int startIndex = verticesIndex();
        double halfHeight = (height / 2);
        double halfWidth = (width / 2);

        double[][] coords =
                { { 0, halfHeight }, { -halfWidth, -halfHeight }, { halfWidth, -halfHeight } };

        double oldX;
        double oldY;

        for (int i = 0; i < coords.length; i++) {
            oldX = coords[i][0];
            oldY = coords[i][1];
            coords[i][0] = (oldX * Math.cos(angle)) - (oldY * Math.sin(angle));
            coords[i][1] = (oldX * Math.sin(angle)) + (oldY * Math.cos(angle));
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

        for (int i = 0; i < 3; i++) {
            mColorsList.add(color[0]);
            mColorsList.add(color[1]);
            mColorsList.add(color[2]);
            mColorsList.add(color[3]);
        }
    }

    private void addCircle(double centreX, double centreY, double width, float[] color) {
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

    private void addEdge(double x1, double y1, double x2, double y2, double thickness,
            boolean arrow, float[] color) {
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

        double[][] coords =
                { { -halfLength, halfThick },
                        { halfLength, halfThick },
                        { -halfLength, -halfThick },
                        { halfLength, -halfThick } };

        for (int i = 0; i < coords.length; i++) {
            oldX = coords[i][0];
            oldY = coords[i][1];
            coords[i][0] = (oldX * Math.cos(lineAngle)) - (oldY * Math.sin(lineAngle));
            coords[i][1] = (oldX * Math.sin(lineAngle)) + (oldY * Math.cos(lineAngle));
        }

        drawSquare(coords[0][0] + xOffset, coords[0][1] + yOffset, coords[1][0] + xOffset,
                coords[1][1] + yOffset, coords[2][0] + xOffset, coords[2][1] + yOffset,
                coords[3][0] + xOffset, coords[3][1] + yOffset, color);

        if (arrow) {
            if (x1 > x2)
                arrowAngle -= Math.PI;
            addTriangle(xOffset, yOffset, 30, 30, arrowAngle - Math.PI / 2, color);
        }
    }

    private void drawSquare(double x, double y, double width, double height, float[] color) {
        float halfWidth = (float) (width / 2);
        float halfHeight = (float) (height / 2);

        drawSquare(x - halfWidth, y - halfHeight, x + halfWidth, y - halfHeight, x - halfWidth, y
                + halfHeight, x + halfWidth, y + halfHeight, color);

    }

    private void drawSquare(double topLeftX, double topLeftY, double topRightX, double topRightY,
            double bottomLeftX, double bottomLeftY, double bottomRightX, double bottomRightY,
            float[] color) {
        int startIndex;
        if (mVerticesList.size() == 0)
            startIndex = 0;
        else
            startIndex = mVerticesList.size() / 2;

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
        float[] vertices2 = new float[(mVerticesList.size() / 2) + mVerticesList.size()];
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
        mGlContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mVertexBuffer);

        mGlContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(vertices2),
                WebGLRenderingContext.STATIC_DRAW);

        // create the colorBuffer
        mColorBuffer = mGlContext.createBuffer();
        mGlContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mColorBuffer);

        mGlContext.bufferData(WebGLRenderingContext.ARRAY_BUFFER, Float32Array.create(colors),
                WebGLRenderingContext.STATIC_DRAW);

        // create the indexBuffer
        mIndexBuffer = mGlContext.createBuffer();
        mGlContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, mIndexBuffer);

        mGlContext.bufferData(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER,
                Uint16Array.create(indices), WebGLRenderingContext.STATIC_DRAW);

        mGlContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mVertexBuffer);
        mGlContext.vertexAttribPointer(mVertexPositionAttribute, 3, WebGLRenderingContext.FLOAT,
                false, 0, 0);

        mGlContext.enableVertexAttribArray(mVertexPositionAttribute);

        mGlContext.bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, mColorBuffer);
        mGlContext.vertexAttribPointer(mVertexColorAttribute, 4, WebGLRenderingContext.FLOAT,
                false, 0, 0);
        mGlContext.enableVertexAttribArray(mVertexColorAttribute);

        mGlContext.bindBuffer(WebGLRenderingContext.ELEMENT_ARRAY_BUFFER, mIndexBuffer);

        // Bind the projection matrix to the shader
        mGlContext.uniformMatrix4fv(mProjectionMatrixUniform, false,
                mProjectionMatrix.getColumnWiseFlatData());

        // Draw the polygon
        mGlContext.drawElements(WebGLRenderingContext.TRIANGLES, indices.length,
                WebGLRenderingContext.UNSIGNED_SHORT, 0);

        mGlContext.flush();

    }

    private float[] createPerspectiveMatrix(int fieldOfViewVertical, float aspectRatio,
            float minimumClearance, float maximumClearance) {

        float top = minimumClearance * (float) Math.tan(fieldOfViewVertical * Math.PI / 360.0);
        float bottom = -top;
        float left = bottom * aspectRatio;
        float right = top * aspectRatio;

        float X = 2 * minimumClearance / (right - left);
        float Y = 2 * minimumClearance / (top - bottom);
        float A = (right + left) / (right - left);
        float B = (top + bottom) / (top - bottom);
        float C = -(maximumClearance + minimumClearance) / (maximumClearance - minimumClearance);
        float D = -2 * maximumClearance * minimumClearance / (maximumClearance - minimumClearance);

        return new float[] { X,
                0.0f,
                A,
                0.0f,
                0.0f,
                Y,
                B,
                0.0f,
                0.0f,
                0.0f,
                C,
                -1.0f,
                0.0f,
                0.0f,
                D,
                0.0f };
    };

    // old 2d canvas functions
    public void renderGraph2d(GWTCanvas canvas, Collection<EdgeDrawable> edges,
            Collection<VertexDrawable> vertices) {

        canvas.setLineWidth(1);
        canvas.setStrokeStyle(Color.BLACK);
        canvas.setFillStyle(Color.BLACK);
        canvas.setFillStyle(Color.WHITE);
        canvas.fillRect(0, 0, 2000, 2000);
        canvas.setFillStyle(Color.BLACK);
        drawGraph(edges, vertices, canvas);

    }

    // Draws a single vertex, currently only draws circular nodes
    private void drawVertex(VertexDrawable vertex, GWTCanvas canvas) {
        double centreX = (vertex.getLeft() + 0.5 * vertex.getWidth() + mOffsetX) * mZoom;
        double centreY = (vertex.getTop() + 0.5 * vertex.getHeight() + mOffsetY) * mZoom;
        double radius = (0.25 * vertex.getWidth()) * mZoom;

        canvas.moveTo(centreX, centreY);
        canvas.beginPath();
        canvas.arc(centreX, centreY, radius, 0, 360, false);
        canvas.closePath();
        canvas.stroke();
        canvas.fill();
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
    }

    // Takes collections of edges and vertices and draws a graph on a specified
    // canvas.
    private void drawGraph(Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices,
            GWTCanvas canvas) {
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
}
