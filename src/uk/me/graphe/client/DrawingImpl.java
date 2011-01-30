package uk.me.graphe.client;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.widgetideas.graphics.client.Color;
import com.google.gwt.widgetideas.graphics.client.GWTCanvas;


public class DrawingImpl implements Drawing {
	
	//used for panning
	public int offsetX, offsetY;

	// JSNI method for webgl, comments omitted because it is one big comment...
    private static native void drawGraph3D(String verticesString, String edgesString) /*-{
            function getPersepctiveMatrix(width, height)
            {
                var fieldOfView = 30.0;
                var aspectRatio = width/height;
                var nearPlane = 1.0;
                var farPlane = 10000.0;
                var top = nearPlane * Math.tan(fieldOfView * Math.PI / 360.0);
                var bottom = -top;
                var right = top * aspectRatio;
                var left = -right;
                var a = (right + left) / (right - left);
                var b = (top + bottom) / (top - bottom);
                var c = (farPlane + nearPlane) / (farPlane - nearPlane);
                var d = (2 * farPlane * nearPlane) / (farPlane - nearPlane);
                var x = (2 * nearPlane) / (right - left);
                var y = (2 * nearPlane) / (top - bottom);
                var perspectiveMatrix = [
                    x, 0, a, 0,
                    0, y, b, 0,
                    0, 0, c, d,
                    0, 0, -1, 0
                ];
                return perspectiveMatrix;
            }
            
            function drawEdge(left1,top1, left2,top2,cWidth,cHeight)
            {

                if(top1 <= top2){
                    var temp1 = left1;
                    var temp2 = top1;
                    left1 = left2;
                    top1 = top2;
                    left2 = temp1;
                    top2 = temp2;
                }
            
                var width = 2;
                var length = Math.sqrt(((top2-top1)*(top2-top1))+((left2-left1)*(left2-left1)));
                var vHeight = top1-top2;
                var hWidth = left1-left2;
                var deg = 180/Math.PI;
                var angle = Math.atan(vHeight/hWidth);
                var angle2 = ((Math.PI/2)-angle);
                var height2 = Math.sin(angle2)*(width/2);
                var width2 = Math.sqrt(((width/2)*(width/2))-(height2*height2));

                if(angle*deg<0)
                {
                    var topRightX = left1-(width2);
                    var topRightY = top1-height2;
                    var topLeftX = left1+(width2);
                    var topLeftY = top1+height2;
                    var bottomRightX = left2-(width2);
                    var bottomRightY = top2-height2;
                    var bottomLeftX = left2+(width2);
                    var bottomLeftY = top2+height2;
                }
                else
                {
                    var topRightX = left1-(width2);
                    var topRightY = top1+height2;
                    var topLeftX = left1+(width2);
                    var topLeftY = top1-height2;
                    var bottomRightX = left2-(width2);
                    var bottomRightY = top2+height2;
                    var bottomLeftX = left2+(width2);
                    var bottomLeftY = top2-height2;
                }
  
                var pvertices = new Float32Array([
                    topRightX,  topRightY, 
                    topLeftX,  topLeftY,  
                    bottomRightX,  bottomRightY, 
                    bottomLeftX,  bottomLeftY
                ]);
                
                drawPolygon(pvertices,canvas.width,canvas.height);
            }
            
            function compareXValue(array1, array2)
            {
              var value1 = array1[0];
              var value2 = array2[0];
              if (value1 > value2) return 1;
              if (value1 < value2) return -1;
              return 0;
            }
            
            function drawVertex(left,top,width,cWidth,cHeight)
            {
                var r = width;
                var numSections =width;
                if(numSections>33)numSections = 33;
                if(numSections<5)numSections = 5;
                var delta_theta = 2.0 * Math.PI / numSections
                var theta = 0;
                var coord = new Array(numSections);
                var pvertices = new Float32Array(numSections*2);
                for (i = 0; i < numSections ; i++) {
                    coord[i] = new Array(2);
                    x = (r * Math.cos(theta))+left;
                    y = (r * Math.sin(theta))+top;
                    x = Math.round(x*1000)/1000
                    y = Math.round(y*1000)/1000
                    coord[i][1] = x;
                    coord[i][0] = y;
                    theta += delta_theta
                }
                coord.sort(compareXValue);
                for(var i = 0;i<numSections-1;i++)
                {
                    if(coord[i][0] == coord[i+1][0] && coord[i][1]<coord[i+1][1])
                    {
                        temp = coord[i];
                        coord[i] = coord[i+1];
                        coord[i+1] = temp;
                    }
                }
                var j = 0;
                
                for (i=0;i<numSections;i++)
                {
                        pvertices[j] = coord[i][1];
                        j++;
                        pvertices[j] = coord[i][0];
                        j++;
                }
                drawPolygon(pvertices,canvas.width,canvas.height);
            }  
            
            
            function drawSquare(left,top,width,cWidth,cHeight)
            {
                left-=width/2;
                top-=width/2;
                var pvertices = new Float32Array([
                    left+width,  top+width, 
                    left,  top+width,  
                    left+width,  top, 
                    left,  top
                ]);
                drawPolygon(pvertices,canvas.width,canvas.height);
            }

            function drawPolygon(pixelVertices,cWidth,cHeight)
            {
                var factor;
                var glWidth = 4.28764;
                var uTop = 8/3;
                var uLeft = -uTop;
                var vertices = new Float32Array(100);
                var vl = 0;
                var modelViewMatrix = [
                    1, 0, 0, 0,
                    0, 1, 0, 0,
                    uLeft, uTop, 1, 0,
                    0, 0, 0, 1
                ];
                if(cWidth>=cHeight) factor = glWidth/cHeight;
                else factor = glWidth/cWidth;
                for(var i=0;i<pixelVertices.length;i++){
                    pixelVertices[i] = (pixelVertices[i])*factor;
                    if (i%2){
                        vertices[vl] = -pixelVertices[i];
                        vl++;
                        vertices[vl] = (4.0);
                    }
                    else
                    {
                        vertices[vl] = (pixelVertices[i]);
                    }
                    vl++;
                }

                gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);
                gl.uniformMatrix4fv(uModelViewMatrix, false, new Float32Array(perspectiveMatrix));
                gl.uniformMatrix4fv(uPerspectiveMatrix, false, new Float32Array(modelViewMatrix));
                gl.drawArrays(gl.TRIANGLE_STRIP, 0, vl / 3.0);
            }
            

            function drawGraph(vertices,edges)
            {

                canvas1 = document.getElementsByTagName("canvas")[1];
                canvas1.style.position = "absolute";
                canvas1.style.zIndex = 10;
                canvas1.style.opacity = 0;
               


                canvas = document.getElementsByTagName("canvas")[0];
                canvas.style.zIndex = 0;
                gl = canvas.getContext("experimental-webgl");
                if(!gl)
                {
                    alert("There's no WebGL context available.");
                    return;
                }

                gl.viewport(0, 0, canvas.width, canvas.height);
                var vertexShaderScript = "attribute vec3 vertexPosition;uniform mat4 modelViewMatrix;uniform mat4 perspectiveMatrix;void main(void) {gl_Position = perspectiveMatrix * modelViewMatrix * vec4(vertexPosition, 1.0);}";
                var vertexShader = gl.createShader(gl.VERTEX_SHADER);
                gl.shaderSource(vertexShader, vertexShaderScript);
                gl.compileShader(vertexShader);

                if(!gl.getShaderParameter(vertexShader, gl.COMPILE_STATUS)) {
                    alert("Couldn't compile the vertex shader");
                    gl.deleteShader(vertexShader);
                    return;
                }

                var fragmentShaderScript = "void main(void) {gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);}";
                var fragmentShader = gl.createShader(gl.FRAGMENT_SHADER);
                gl.shaderSource(fragmentShader, fragmentShaderScript);
                gl.compileShader(fragmentShader);

                if(!gl.getShaderParameter(fragmentShader, gl.COMPILE_STATUS)) {
                    alert("error");
                    gl.deleteShader(fragmentShader);
                    return;
                }

                gl.program = gl.createProgram();
                gl.attachShader(gl.program, vertexShader);
                gl.attachShader(gl.program, fragmentShader);
                gl.linkProgram(gl.program);

                if (!gl.getProgramParameter(gl.program, gl.LINK_STATUS)) {
                    alert("Cant initialise shaders");
                    gl.deleteProgram(gl.program);
                    gl.deleteProgram(vertexShader);
                    gl.deleteProgram(fragmentShader);
                    return;
                }

                gl.useProgram(gl.program);
                var vertexPosition = gl.getAttribLocation(gl.program, "vertexPosition");
                gl.enableVertexAttribArray(vertexPosition);
                gl.clearColor(1.0, 1.0, 1.0, 1.0);
                gl.clearDepth(1.0);
                gl.enable(gl.DEPTH_TEST);                       
                gl.depthFunc(gl.LEQUAL);
                perspectiveMatrix = getPersepctiveMatrix(canvas.width,canvas.height);
                
                
                
                gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);  
                var vertexBuffer = gl.createBuffer();
                gl.bindBuffer(gl.ARRAY_BUFFER, vertexBuffer);
                var vertexPosAttribLocation = gl.getAttribLocation(gl.program, "vertexPosition");
                gl.vertexAttribPointer(vertexPosAttribLocation, 3.0, gl.FLOAT, false, 0, 0);
                uModelViewMatrix = gl.getUniformLocation(gl.program, "modelViewMatrix");
                uPerspectiveMatrix = gl.getUniformLocation(gl.program, "perspectiveMatrix");
                
                
                var edgesArray=edges.split(",");
                for(var i=0;i<edgesArray.length-4;i+=4)
                {
                    var left1 = parseInt(edgesArray[i]);
                    var top1 = parseInt(edgesArray[i+1]);
                    var left2 = parseInt(edgesArray[i+2]);
                    var top2 = parseInt(edgesArray[i+3]);
                    drawEdge( left1,top1,left2,top2,canvas.width,canvas.height);
                }
                
                var verticesArray=vertices.split(",");
                for(var i=0;i<verticesArray.length-3;i+=3)
                {
                    var left = parseInt(verticesArray[i]);
                    var top = parseInt(verticesArray[i+1]);
                    var width = parseInt(verticesArray[i+2]);
                    drawVertex(left,top,width,canvas.width,canvas.height);  
                }
                

                gl.flush();

            }

            drawGraph(verticesString,edgesString)
           
    }-*/;
    
    private static native int webglCheck()/*-{
        if (typeof Float32Array != "undefined")return 1;
        return 0;
    }-*/;
    

    public void renderGraph (GWTCanvas canvas, Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices) {
        
        // Do a (kind of reliable) check for webgl
        if(webglCheck() == 1)
        {
            // Can do WebGL
            
            // At the moment coordinates are passed to JavaScript as comma separated strings
            // This will most probably change in the future.
            String edgesString = "";
            String veticesString = "";
            String separator = ",";
            for (EdgeDrawable thisEdge : edges) {
                double startX = thisEdge.getStartX() + offsetX;
                double startY = thisEdge.getStartY() + offsetY;
                double endX = thisEdge.getEndX() + offsetX;
                double endY = thisEdge.getEndY() + offsetY;
                edgesString += startX+separator+startY+separator+endX+separator+endY+separator;
            }
            for (VertexDrawable thisVertex : vertices) {
                double centreX = thisVertex.getLeft() + 0.5*thisVertex.getWidth() + offsetX;
                double centreY = thisVertex.getTop() + 0.5*thisVertex.getHeight() + offsetY;
                double width = 0.5*thisVertex.getWidth();
                veticesString += centreX+separator+centreY+separator+width+separator;
            }
            
            // JSNI method used to draw webGL graph version
            drawGraph3D(veticesString, edgesString);
        }
        else
        {
            // Cant do webGL so draw on 2d Canvas
            renderGraph2d(canvas,edges,vertices);
            
        }

    }
    

    public void renderGraph2d(GWTCanvas canvas, Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices) {
    
        // Testing data, tests drawing function
        Collection<VertexDrawable> collection1 = new ArrayList<VertexDrawable>();
        Collection<EdgeDrawable> collection2 = new ArrayList<EdgeDrawable>();
        VertexDrawable v1 = new VertexDrawable(10,10,10,10,"v1");
        VertexDrawable v2 = new VertexDrawable(20,20,10,10,"v2");
        VertexDrawable v3 = new VertexDrawable(70,70,10,10,"v3");
        VertexDrawable v4 = new VertexDrawable(100,100,10,10,"v4");
        collection1.add(v1);
        collection1.add(v2);
        collection1.add(v3);
        collection1.add(v4);
        EdgeDrawable e1 = new EdgeDrawable(10, 10, 50, 50);
        EdgeDrawable e2 = new EdgeDrawable(100, 100, 150, 250);
        collection2.add(e1);
        collection2.add(e2);
        // End of test function
        
        // Set style of canvas
        canvas.clear();
        canvas.setLineWidth(1);
        canvas.setStrokeStyle(Color.BLACK);
        canvas.setFillStyle(Color.BLACK);
        canvas.setBackgroundColor(Color.WHITE);
        drawGraph(edges, vertices, canvas);
        
    }
    
    // Draws a single vertex, currently only draws circular nodes  
    private void drawVertex(VertexDrawable vertex, GWTCanvas canvas) {
        double centreX = vertex.getLeft() + 0.5*vertex.getWidth() + offsetX;
        double centreY = vertex.getTop() + 0.5*vertex.getHeight() + offsetY;
        double radius = 0.5*vertex.getWidth();
        
        canvas.moveTo(centreX, centreY);
        canvas.beginPath();
        canvas.arc(centreX,centreY,radius,0,360,false);
        canvas.closePath();
        canvas.stroke();
        canvas.fill();
    }
  
    // Draws a line from coordinates to other coordinates  
    private void drawEdge(EdgeDrawable edge, GWTCanvas canvas) { 
        double startX = edge.getStartX() + offsetX;
        double startY = edge.getStartY() + offsetY;
        double endX = edge.getEndX() + offsetX;
        double endY = edge.getEndY() + offsetY;
        
        canvas.beginPath();
        canvas.moveTo(startX,startY);
        canvas.lineTo(endX,endY);
        canvas.closePath();
        canvas.stroke();
    }
 
    // Takes collections of edges and vertices and draws a graph on a specified canvas.
    private void drawGraph(Collection<EdgeDrawable> edges, Collection<VertexDrawable> vertices,
            GWTCanvas canvas) {
        for (EdgeDrawable thisEdge : edges) {
            drawEdge(thisEdge, canvas);
        }
        for (VertexDrawable thisVertex : vertices) {
            drawVertex(thisVertex, canvas);
        }
    }

    
    //set offset in the event of a pan
    public void setOffset(int x, int y){
    	offsetX = x;
    	offsetY = y;
    }
    
    //getters for offsets
    public int getOffsetX(){
    	return offsetX;
    }
    
    public int getOffsetY(){
    	return offsetY;
    }
}
