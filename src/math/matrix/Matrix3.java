/*
 * File:    Matrix3.java
 * Package: math.matrix
 * Author:  Zachary Gill
 */

package math.matrix;

import math.vector.Vector;

/**
 * Defines a 3D Matrix.
 */
public class Matrix3 {
    
    //Fields
    
    /**
     * The elements of the matrix.
     */
    public double[] values;
    
    
    //Constructors
    
    /**
     * The constructor for a 3D Matrix.
     *
     * @param values The elements of the matrix.
     */
    public Matrix3(double[] values) {
        this.values = values;
    }
    
    
    //Methods
    
    /**
     * Multiplies the 3D matrix by another 3D matrix.
     *
     * @param other The other 3D matrix.
     * @return The 3D matrix result of the multiplication.
     */
    public Matrix3 multiply(Matrix3 other) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int i = 0; i < 3; i++) {
                    result[row * 3 + col] += values[row * 3 + i] * other.values[i * 3 + col];
                }
            }
        }
        return new Matrix3(result);
    }
    
    /**
     * Multiplies the 3D matrix by a vector.
     *
     * @param other The vector.
     * @return The vector result of the multiplication.
     *
     * @throws ArithmeticException When the vector is not of the proper size.
     */
    public Vector multiply(Vector other) throws ArithmeticException {
        if (other.getDimension() != 3) {
            throw new ArithmeticException("The vector: " + other + " is of improper size for multiplication with a 3D matrix.");
        }
        
        double[] result = new double[3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row] += values[row * 3 + col] * other.get(col);
            }
        }
        return new Vector(result[0], result[1], result[2]);
    }
    
    /**
     * Scales the 3D matrix by the values of another 3D matrix.
     *
     * @param scalars The 3D matrix of scalars.
     * @return The 3D matrix result of the scaling.
     */
    public Matrix3 scale(Matrix3 scalars) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row * 3 + col] = values[row * 3 + col] * scalars.values[row * 3 + col];
            }
        }
        return new Matrix3(result);
    }
    
    /**
     * Scales the 3D matrix by a value.
     *
     * @param scalar The value to scale by.
     * @return The 3D matrix result of the scaling.
     */
    public Matrix3 scale(double scalar) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row * 3 + col] = values[row * 3 + col] * scalar;
            }
        }
        return new Matrix3(result);
    }
    
    /**
     * Determines the determinant of the matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        return (values[0] * ((values[4] * values[8]) - (values[5] * values[7]))) -
                (values[1] * ((values[3] * values[8]) - (values[5] * values[6]))) +
                (values[2] * ((values[3] * values[7]) - (values[4] * values[6])));
    }
    
    /**
     * Determines the minors of the matrix.
     *
     * @return The minors of the matrix.
     */
    public Matrix3 minors() {
        return new Matrix3(new double[] {
                subMatrix2(4, 5, 7, 8).determinant(), subMatrix2(3, 5, 6, 8).determinant(), subMatrix2(3, 4, 6, 7).determinant(),
                subMatrix2(1, 2, 7, 8).determinant(), subMatrix2(0, 2, 6, 8).determinant(), subMatrix2(0, 1, 6, 7).determinant(),
                subMatrix2(1, 2, 4, 5).determinant(), subMatrix2(0, 2, 3, 5).determinant(), subMatrix2(0, 1, 3, 4).determinant()
        });
    }
    
    /**
     * Transposes the matrix.
     *
     * @return The transpose of the matrix.
     */
    public Matrix3 transpose() {
        return new Matrix3(new double[] {
                values[0], values[3], values[6],
                values[1], values[4], values[7],
                values[2], values[5], values[8]
        });
    }
    
    /**
     * Cofactors of the matrix.
     *
     * @return The cofactored matrix.
     */
    public Matrix3 cofactor() {
        return scale(new Matrix3(new double[] {
                1, -1, 1,
                -1, 1, -1,
                1, -1, 1
        }));
    }
    
    /**
     * Solves the matrix as a system of equations.
     *
     * @param solutionVector The solution vector.
     * @return The solution to the system of equations.
     *
     * @throws ArithmeticException When the solution vector is not of the proper size.
     */
    public Vector solveSystem(Vector solutionVector) throws ArithmeticException {
        Matrix3 system = minors();
        system = system.cofactor();
        system = system.transpose();
        system = system.scale(1.0 / determinant());
        return system.multiply(solutionVector);
    }
    
    /**
     * Calculates the inverse of the matrix.
     *
     * @return The inverse of the matrix.
     *
     * @throws ArithmeticException If the matrix cannot be inverted.
     */
    public Matrix3 inverse() throws ArithmeticException {
        double determinant = determinant();
        if (determinant == 0) {
            throw new ArithmeticException();
        }
        return transpose().minors().cofactor().scale(1.0 / determinant);
    }
    
    /**
     * Transforms a vector using the matrix.
     *
     * @param in The vector to transform.
     * @return The transformed vector.
     *
     * @throws ArithmeticException When the vector is not of the proper size.
     */
    public Vector transform(Vector in) throws ArithmeticException {
        if (in.getDimension() != 3) {
            throw new ArithmeticException("The vector: " + in + " is of improper size for transformation with a 3D matrix.");
        }
        
        return new Vector(
                in.get(0) * values[0] + in.get(1) * values[3] + in.get(2) * values[6],
                in.get(0) * values[1] + in.get(1) * values[4] + in.get(2) * values[7],
                in.get(0) * values[2] + in.get(1) * values[5] + in.get(2) * values[8]
        );
    }
    
    /**
     * Creates a 2D sub-matrix of the matrix.
     *
     * @param a11 The index of the first element for the sub-matrix.
     * @param a12 The index of the second element for the sub-matrix.
     * @param a21 The index of the third element for the sub-matrix.
     * @param a22 The index of the fourth element for the sub-matrix.
     * @return The 2D sub-matrix.
     */
    public Matrix2 subMatrix2(int a11, int a12, int a21, int a22) {
        return new Matrix2(new double[] {
                values[a11], values[a12],
                values[a21], values[a22]
        });
    }
    
}