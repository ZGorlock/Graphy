/*
 * File:    Matrix2.java
 * Package: math.matrix
 * Author:  Zachary Gill
 */

package math.matrix;

public class Matrix2 {
    
    public double[] values;
    
    public Matrix2(double[] values) {
        this.values = values;
    }
    
    public double determinant() {
        return (values[0] * values[3]) - (values[1] * values[2]);
    }
    
}
