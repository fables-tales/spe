/**   
 * Copyright 2009-2010 Sönke Sothmann, Steffen Schäfer and others
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.me.graphe.client.webglUtil;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import uk.me.graphe.client.webglUtil.math.FloatMatrix;

/**
 * Widget to display a FloatMatrix.
 * 
 * @author Sönke Sothmann
 * @author Steffen Schäfer
 * 
 */
public class MatrixWidget extends Composite {

	private int width;
	private int height;
	private FlexTable flextable;
	static private NumberFormat formatter = NumberFormat
			.getFormat("#########0.00");

	/**
	 * Constructs a new instance of the MatrixWidget to view a FloatMatrix of
	 * the given size.
	 * 
	 * @param width
	 *            column count of matrix
	 * @param height
	 *            row count of matrix
	 * @param title
	 *            the title of the matrix
	 */
	public MatrixWidget(int width, int height, String title) {
		this.width = width;
		this.height = height;
		flextable = new FlexTable();
		flextable.setWidget(0, 0, new Label(title));
		flextable.getFlexCellFormatter().setColSpan(0, 0, width);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				flextable.setText(y + 1, x, "[" + String.valueOf(x) + "_"
						+ String.valueOf(y) + "]");
			}
		}

		initWidget(flextable);

		setStyleName("matrixWidget");
	}

	/**
	 * Set widget display data to data of the given FloatMatrix
	 * 
	 * @param matrix
	 */
	public void setData(FloatMatrix matrix) {
		if (matrix == null) {
			return;
		}
		float[][] data = matrix.getData();
		for (int spalte = 0; spalte < width; spalte++) {
			for (int zeile = 0; zeile < height; zeile++) {
				flextable.setText(zeile + 1, spalte, "["
						+ formatter.format(data[zeile][spalte]) + "]");
			}
		}
	}
}
