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
package uk.me.graphe.client.webglUtil.math;

import java.util.Arrays;

/**
 * TODO should we use a "standard" Java matrix and vector api? Does it work in
 * GWT?
 * 
 * Representation of a matrix constructed of float fields. The first dimension
 * of the data array represents the height of the Matrix and the second
 * dimension represents the width. So the matrix is saved line-wise.
 * 
 * @author Steffen Schäfer
 * @author Sönke Sothmann
 * 
 */
public class FloatMatrix {

	/**
	 * The data containing the values of the matix. The format is [row][column].
	 */
	protected float[][] data;
	/**
	 * The width of the matrix.
	 */
	protected final int width;
	/**
	 * The heigth of the matrix.
	 */
	protected final int height;

	/**
	 * Constructs a square FloatMatrix with width and height = size.
	 * 
	 * @param size
	 *            the width and height of the FloatMatrix
	 */
	public FloatMatrix(int size) {
		this(size, size);
	}

	/**
	 * Constructs a FloatMatrix with the given width and height.
	 * 
	 * @param width
	 *            the width of the FloatMatrix
	 * @param height
	 *            the height of the FloatMatrix
	 */
	public FloatMatrix(int width, int height) {
		this.width = width;
		this.height = height;
		data = new float[height][width];
	}

	/**
	 * Constructs a new FloatMatrix with the given data to set.
	 * 
	 * @param newData
	 *            the data to use
	 */
	public FloatMatrix(float[][] newData) {
		this(newData[0].length, newData.length);
		setData(newData);
	}

	/**
	 * Sets the data of the FloatMatrix in a flat row-wise representation.
	 * 
	 * @param newData
	 *            the new data to set
	 */
	public void setData(float... newData) {
		if (newData.length != width * height) {
			throw new IllegalArgumentException(
					"The given data array has an incorrect size.");
		}
		for (int i = 0; i < height; i++) {
			System.arraycopy(newData, i * width, data[i], 0, width);
		}
	}

	/**
	 * Sets the new data of the FloatMatrix.
	 * 
	 * @param newData
	 *            the new data to set
	 */
	public void setData(float[][] newData) {
		if (newData.length != height) {
			throw new IllegalArgumentException(
					"The given data array has an incorrect size.");
		}
		for (int i = 0; i < height; i++) {
			if (newData[i].length != width) {
				throw new IllegalArgumentException(
						"The given data array has an incorrect size.");
			}
			System.arraycopy(newData[i], 0, data[i], 0, width);
		}
	}

	/**
	 * returns a copy of the inner data.
	 * 
	 * @return 2 dimensional data array - [row][column]
	 */
	public float[][] getData() {
		float[][] result = new float[height][width];
		for (int i = 0; i < height; i++) {
			System.arraycopy(data[i], 0, result[i], 0, width);
			// doesn't work in GWT
			// result[i] = Arrays.copyOf(data[i], width);
		}
		return result;
	}

	/**
	 * Returns the data in a flat row-wise representation.
	 * 
	 * @return the flat data as array
	 */
	public float[] getFlatData() {
		float[] flatDta = new float[height * height];
		for (int i = 0; i < height; i++) {
			System.arraycopy(data[i], 0, flatDta, i * width, width);
		}
		return flatDta;
	}

	/**
	 * Returns the data in a flat column-wise representation.
	 * 
	 * @return the flat data as array
	 */
	public float[] getColumnWiseFlatData() {
		float[] flatDta = new float[height * height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				flatDta[i * height + j] = data[j][i];
			}
		}
		return flatDta;
	}

	/**
	 * Creates a new FloatMatrix that is the transposed to this FloatMatrix.
	 * 
	 * @return the transposed matrix
	 */
	public FloatMatrix transpose() {
		FloatMatrix transposed = new FloatMatrix(height, width);
		transposeImpl(transposed);
		return transposed;
	}

	/**
	 * The inner impl of the transpose method.
	 * 
	 * @param result
	 */
	protected void transposeImpl(FloatMatrix result) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				result.data[j][i] = this.data[i][j];
			}
		}
	}

	/**
	 * Creates a new FloatMatrix that is the result of the multiplication of
	 * this and the other FloatMatrix.
	 * 
	 * @param other
	 * @return the multiplied FloatMatrix
	 */
	public FloatMatrix multiply(FloatMatrix other) {
		if (this.width != other.height)
			throw new RuntimeException(
					"The width of this matrix must match the height of the other matrix!");
		FloatMatrix result = new FloatMatrix(other.width, this.height);
		multiplyImpl(other, result);

		return result;
	}

	/**
	 * Impl of the multiply method.
	 * 
	 * @param other
	 * @param result
	 */
	protected void multiplyImpl(FloatMatrix other, FloatMatrix result) {
		for (int i = 0; i < other.width; i++) {
			for (int j = 0; j < this.height; j++) {
				for (int k = 0; k < this.width; k++) {
					result.data[j][i] += (this.data[j][k] * other.data[k][i]);
				}
			}
		}
	}

	/**
	 * Main method for simple testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FloatMatrix m1 = new FloatMatrix(new float[][] { new float[] { 1, 2 },
				new float[] { 3, 4 } });
		FloatMatrix m2 = new FloatMatrix(new float[][] { new float[] { 5, 6 },
				new float[] { 7, 8 } });

		System.out.println(Arrays.toString(m1.multiply(m2).getFlatData()));
	}

}
