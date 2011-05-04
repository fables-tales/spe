package uk.me.graphe.client.webglUtil.math;

/**
 * Represents a 4x4 matrix.
 * 
 * @author Sönke Sothmann
 * 
 */
public class FloatMatrix4x4 extends FloatMatrix {

	/**
	 * Constructs a new instance of the FloatMatrix4x4.
	 */
	public FloatMatrix4x4() {
		super(4, 4);
	}

	/**
	 * Constructs a new instance of the FloatMatrix4x4 with the given data to
	 * set.
	 * 
	 * @param data
	 *            the data to set
	 */
	public FloatMatrix4x4(float[][] data) {
		super(data);
		if ((data.length != 4) || (data[0].length != 4)
				|| (data[1].length != 4) || (data[2].length != 4)
				|| (data[3].length != 4)) {
			throw new IllegalArgumentException("matrix dimensions must be 4x4");
		}
	}

	/**
	 * Creates a new FloatMatrix4x4 that is the result of the multiplication of
	 * this and the other FloatMatrix.
	 * 
	 * @param other
	 * @return the multiplied FloatMatrix
	 */
	public FloatMatrix4x4 multiply(FloatMatrix4x4 other) {
		FloatMatrix4x4 result = new FloatMatrix4x4();
		multiplyImpl(other, result);
		return result;
	}

	/**
	 * Creates a new FloatMatrix4x4 that is the transposed to this FloatMatrix.
	 * 
	 * @return the transposed matrix
	 */
	@Override
	public FloatMatrix4x4 transpose() {
		FloatMatrix4x4 transposed = new FloatMatrix4x4();
		transposeImpl(transposed);
		return transposed;
	}

	/**
	 * Calculate determinant of matrix
	 * 
	 * @return determinant
	 */
	public float det() {
		if (this.width != 4 || this.height != 4) {
			throw new UnsupportedOperationException(
					"calculation of det is only supported for 4x4 matrices");
		}
		float det = data[0][0] * data[1][1] * data[2][2] * data[3][3]
				+ data[0][0] * data[1][2] * data[2][3] * data[3][1]
				+ data[0][0] * data[1][3] * data[2][1] * data[3][2]
				+ data[0][1] * data[1][0] * data[2][3] * data[3][2]
				+ data[0][1] * data[1][2] * data[2][0] * data[3][3]
				+ data[0][1] * data[1][3] * data[2][2] * data[3][0]
				+ data[0][2] * data[1][0] * data[2][1] * data[3][3]
				+ data[0][2] * data[1][1] * data[2][3] * data[3][0]
				+ data[0][2] * data[1][3] * data[2][0] * data[3][1]
				+ data[0][3] * data[1][0] * data[2][2] * data[3][1]
				+ data[0][3] * data[1][1] * data[2][0] * data[3][2]
				+ data[0][3] * data[1][2] * data[2][1] * data[3][0]
				- data[0][0] * data[1][1] * data[2][3] * data[3][2]
				- data[0][0] * data[1][2] * data[2][1] * data[3][3]
				- data[0][0] * data[1][3] * data[2][2] * data[3][1]
				- data[0][1] * data[1][0] * data[2][2] * data[3][3]
				- data[0][1] * data[1][2] * data[2][3] * data[3][0]
				- data[0][1] * data[1][3] * data[2][0] * data[3][2]
				- data[0][2] * data[1][0] * data[2][3] * data[3][1]
				- data[0][2] * data[1][1] * data[2][0] * data[3][3]
				- data[0][2] * data[1][3] * data[2][1] * data[3][0]
				- data[0][3] * data[1][0] * data[2][1] * data[3][2]
				- data[0][3] * data[1][1] * data[2][2] * data[3][0]
				- data[0][3] * data[1][2] * data[2][0] * data[3][1];
		return det;
	}

	/**
	 * Calculate inverse matrix
	 * 
	 * @return inverse matrix
	 */
	public FloatMatrix4x4 inverse() {
		if (this.width != 4 || this.height != 4) {
			throw new UnsupportedOperationException(
					"inverse is only supported for 4x4 matrices");
		}
		float det = det();
		if (det == 0) {
			throw new IllegalStateException(
					"inverse cannot be calculated because det is 0");
		}

		float b00, b01, b02, b03, b10, b11, b12, b13, b20, b21, b22, b23, b30, b31, b32, b33;
		b00 = data[1][1] * data[2][2] * data[3][3] + data[1][2] * data[2][3]
				* data[3][1] + data[1][3] * data[2][1] * data[3][2]
				- data[1][1] * data[2][3] * data[3][2] - data[1][2]
				* data[2][1] * data[3][3] - data[1][3] * data[2][2]
				* data[3][1];
		b01 = data[0][1] * data[2][3] * data[3][2] + data[0][2] * data[2][1]
				* data[3][3] + data[0][3] * data[2][2] * data[3][1]
				- data[0][1] * data[2][2] * data[3][3] - data[0][2]
				* data[2][3] * data[3][1] - data[0][3] * data[2][1]
				* data[3][2];
		b02 = data[0][1] * data[1][2] * data[3][3] + data[0][2] * data[1][3]
				* data[3][1] + data[0][3] * data[1][1] * data[3][2]
				- data[0][1] * data[1][3] * data[3][2] - data[0][2]
				* data[1][1] * data[3][3] - data[0][3] * data[1][2]
				* data[3][1];
		b03 = data[0][1] * data[1][3] * data[2][2] + data[0][2] * data[1][1]
				* data[2][3] + data[0][3] * data[1][2] * data[2][1]
				- data[0][1] * data[1][2] * data[2][3] - data[0][2]
				* data[1][3] * data[2][1] - data[0][3] * data[1][1]
				* data[2][2];
		b10 = data[1][0] * data[2][3] * data[3][2] + data[1][2] * data[2][0]
				* data[3][3] + data[1][3] * data[2][2] * data[3][0]
				- data[1][0] * data[2][2] * data[3][3] - data[1][2]
				* data[2][3] * data[3][0] - data[1][3] * data[2][0]
				* data[3][2];
		b11 = data[0][0] * data[2][2] * data[3][3] + data[0][2] * data[2][3]
				* data[3][0] + data[0][3] * data[2][0] * data[3][2]
				- data[0][0] * data[2][3] * data[3][2] - data[0][2]
				* data[2][0] * data[3][3] - data[0][3] * data[2][2]
				* data[3][0];
		b12 = data[0][0] * data[1][3] * data[3][2] + data[0][2] * data[1][0]
				* data[3][3] + data[0][3] * data[1][2] * data[3][0]
				- data[0][0] * data[1][2] * data[3][3] - data[0][2]
				* data[1][3] * data[3][0] - data[0][3] * data[1][0]
				* data[3][2];
		b13 = data[0][0] * data[1][2] * data[2][3] + data[0][2] * data[1][3]
				* data[2][0] + data[0][3] * data[1][0] * data[2][2]
				- data[0][0] * data[1][3] * data[2][2] - data[0][2]
				* data[1][0] * data[2][3] - data[0][3] * data[1][2]
				* data[2][0];
		b20 = data[1][0] * data[2][1] * data[3][3] + data[1][1] * data[2][3]
				* data[3][0] + data[1][3] * data[2][0] * data[3][1]
				- data[1][0] * data[2][3] * data[3][1] - data[1][1]
				* data[2][0] * data[3][3] - data[1][3] * data[2][1]
				* data[3][0];
		b21 = data[0][0] * data[2][3] * data[3][1] + data[0][1] * data[2][0]
				* data[3][3] + data[0][3] * data[2][1] * data[3][0]
				- data[0][0] * data[2][1] * data[3][3] - data[0][1]
				* data[2][3] * data[3][0] - data[0][3] * data[2][0]
				* data[3][1];
		b22 = data[0][0] * data[1][1] * data[3][3] + data[0][1] * data[1][3]
				* data[3][0] + data[0][3] * data[1][0] * data[3][1]
				- data[0][0] * data[1][3] * data[3][1] - data[0][1]
				* data[1][0] * data[3][3] - data[0][3] * data[1][1]
				* data[3][0];
		b23 = data[0][0] * data[1][3] * data[2][1] + data[0][1] * data[1][0]
				* data[2][3] + data[0][3] * data[1][1] * data[2][0]
				- data[0][0] * data[1][1] * data[2][3] - data[0][1]
				* data[1][3] * data[2][0] - data[0][3] * data[1][0]
				* data[2][1];
		b30 = data[1][0] * data[2][2] * data[3][1] + data[1][1] * data[2][0]
				* data[3][2] + data[1][2] * data[2][1] * data[3][0]
				- data[1][0] * data[2][1] * data[3][2] - data[1][1]
				* data[2][2] * data[3][0] - data[1][2] * data[2][0]
				* data[3][1];
		b31 = data[0][0] * data[2][1] * data[3][2] + data[0][1] * data[2][2]
				* data[3][0] + data[0][2] * data[2][0] * data[3][1]
				- data[0][0] * data[2][2] * data[3][1] - data[0][1]
				* data[2][0] * data[3][2] - data[0][2] * data[2][1]
				* data[3][0];
		b32 = data[0][0] * data[1][2] * data[3][1] + data[0][1] * data[1][0]
				* data[3][2] + data[0][2] * data[1][1] * data[3][0]
				- data[0][0] * data[1][1] * data[3][2] - data[0][1]
				* data[1][2] * data[3][0] - data[0][2] * data[1][0]
				* data[3][1];
		b33 = data[0][0] * data[1][1] * data[2][2] + data[0][1] * data[1][2]
				* data[2][0] + data[0][2] * data[1][0] * data[2][1]
				- data[0][0] * data[1][2] * data[2][1] - data[0][1]
				* data[1][0] * data[2][2] - data[0][2] * data[1][1]
				* data[2][0];

		FloatMatrix4x4 result = new FloatMatrix4x4(new float[][] {
				new float[] { b00, b01, b02, b03 },
				new float[] { b10, b11, b12, b13 },
				new float[] { b20, b21, b22, b23 },
				new float[] { b30, b31, b32, b33 } });
		return result;
	}
}
