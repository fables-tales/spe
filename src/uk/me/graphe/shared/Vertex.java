package uk.me.graphe.shared;

public class Vertex {
	private String mLabel;

	@Override
    public int hashCode() {
        return mLabel.hashCode();
    }

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
	
	@Override
	public String toString() {
	    return this.getLabel();
	}
	
}
