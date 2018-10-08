package math.matrix;

import math.vector.Vector;

public class Matrix3 {
    
    //TODO document
    //TODO throw Arithmetic exceptions for Vectors of bad length
    
    public double[] values;
    
    public Matrix3(double[] values) {
        this.values = values;
    }
    
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
    
    public Vector multiply(Vector other) {
        double[] result = new double[3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row] += values[row * 3 + col] * other.get(col);
            }
        }
        return new Vector(result[0], result[1], result[2]);
    }
    
    public Matrix3 scale(Matrix3 scalars) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row * 3 + col] = values[row * 3 + col] * scalars.values[row * 3 + col];
            }
        }
        return new Matrix3(result);
    }
    
    public Matrix3 scale(double scalar) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row * 3 + col] = values[row * 3 + col] * scalar;
            }
        }
        return new Matrix3(result);
    }
    
    public double determinant() {
        return (values[0] * ((values[4] * values[8]) - (values[5] * values[7]))) -
               (values[1] * ((values[3] * values[8]) - (values[5] * values[6]))) +
               (values[2] * ((values[3] * values[7]) - (values[4] * values[6])));
    }
    
    public Matrix3 minors() {
        return new Matrix3(new double[] {
                subMatrix2(4, 5, 7, 8).determinant(), subMatrix2(3, 5, 6, 8).determinant(), subMatrix2(3, 4, 6, 7).determinant(),
                subMatrix2(1, 2, 7, 8).determinant(), subMatrix2(0, 2, 6, 8).determinant(), subMatrix2(0, 1, 6, 7).determinant(),
                subMatrix2(1, 2, 4, 5).determinant(), subMatrix2(0, 2, 3, 5).determinant(), subMatrix2(0, 1, 3, 4).determinant()
        });
    }
    
    public Matrix3 transpose() {
        return new Matrix3(new double[] {
                values[0], values[3], values[6],
                values[1], values[4], values[7],
                values[2], values[5], values[8]
        });
    }
    
    public Matrix3 cofactor() {
        return scale(new Matrix3(new double[] {
                1, -1, 1,
                -1, 1, -1,
                1, -1, 1
        }));
    }
    
    public Vector solveSystem(Vector solutionVector) {
        Matrix3 system = minors();
        system = system.cofactor();
        system = system.transpose();
        system = system.scale(1 / determinant());
        return system.multiply(solutionVector);
    }
    
    public Vector transform(Vector in) {
        return new Vector(
                in.get(0) * values[0] + in.get(1) * values[3] + in.get(2) * values[6],
                in.get(0) * values[1] + in.get(1) * values[4] + in.get(2) * values[7],
                in.get(0) * values[2] + in.get(1) * values[5] + in.get(2) * values[8]
        );
    }
    
    public Matrix2 subMatrix2(int a11, int a12, int a21, int a22) {
        return new Matrix2(new double[] {
                values[a11], values[a12],
                values[a21], values[a22]
        });
    }
    
}