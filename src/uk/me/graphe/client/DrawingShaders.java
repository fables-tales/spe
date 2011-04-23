package uk.me.graphe.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface DrawingShaders extends ClientBundle {
        
        public static DrawingShaders INSTANCE = GWT.create(DrawingShaders.class);

        @Source(value = { "fragment-shader.txt" })
        TextResource fragmentShader();

        @Source(value = { "vertex-shader.txt" })
        TextResource vertexShader();

}

