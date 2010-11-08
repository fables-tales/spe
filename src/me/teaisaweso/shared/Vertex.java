package me.teaisaweso.shared;

public class Vertex {
	private String mLabel;
	
	/**
	 * creates a vertex with a label
	 * @param label
	 */
	public Vertex(String label) {
	    mLabel = label;
	}
	
	/**
	 * gets the label of this vertex
	 * @return
	 */
	public String getLabel() {
	    return mLabel;
	}
}
