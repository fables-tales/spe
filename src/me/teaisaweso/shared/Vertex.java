package me.teaisaweso.shared;

public class Vertex {
	private String mLabel;

	/**
	 * creates a vertex with a label
	 * 
	 * @param label
	 */
	public Vertex(String label) {
		mLabel = label;
	}

	/**
	 * gets the label of this vertex
	 * 
	 * @return
	 */
	public String getLabel() {
		return mLabel;
	}

	@Override
	public boolean equals(Object o) {
		/*
		 * o is the same vertex if: * o is a vertex instance * o has the same
		 * label as this vertex
		 */

		return (o instanceof Vertex && ((Vertex) o).getLabel().equals(mLabel));
	}
}
