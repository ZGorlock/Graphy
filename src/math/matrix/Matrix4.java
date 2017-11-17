/*
 * File:    Matrix4.java
 * Package: math.matrix
 * Author:  Zachary Gill
 */

package math.matrix;

import math.vector.Vector;

public class Matrix4
{
    public double[] values;
    
    public Matrix4(double[] values) {
        this.values = values;
    }
    
    
    public Matrix4 multiply(Matrix4 other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result[row * 4 + col] += values[row * 4 + i] * other.values[i * 4 + col];
                }
            }
        }
        return new Matrix4(result);
    }
    
    public Vector multiply(Vector other)
    {
        double[] result = new double[4];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result[row] += values[row * 4 + col] * other.get(col);
            }
        }
        return new Vector(result[0], result[1], result[2], result[3]);
    }
    
}
