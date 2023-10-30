package com.nighthawk.spring_portfolio.mvc.models;

import org.apache.commons.math3.linear.*;
import java.util.Arrays;

public class MultiVarAnalyticsGradeRegression {
    public static void main(String[] args) {
        // Mock data representing GitHub analytics for each student
        double[][] xData = {
            {10, 2, 500, 100},  // {Commits, Repositories Contributed To, Additions, Deletions} for student 1
            {15, 3, 700, 150},
            {12, 1, 650, 120},
            {8,  2, 400, 80},
            {20, 4, 900, 180}
        };
        double[] yData = {85, 90, 87, 80, 95};  // Predicted grades for the students based on their GitHub activity

        // Calculate coefficients
        double[] coefficients = calculateCoefficients(xData, yData);

        System.out.println("Coefficients: " + Arrays.toString(coefficients));
    }

    public static double[] calculateCoefficients(double[][] xData, double[] yData) {
        int n = xData.length;
        int m = xData[0].length;

        // Construct matrix X and vector Y
        RealMatrix X = new Array2DRowRealMatrix(n, m + 1);
        RealVector Y = new ArrayRealVector(yData, false);

        for (int i = 0; i < n; i++) {
            X.setEntry(i, 0, 1);  // Bias term
            for (int j = 0; j < m; j++) {
                X.setEntry(i, j + 1, xData[i][j]);
            }
        }

        // Calculate coefficients using the formula: (X^T * X + lambda*I)^(-1) * X^T * Y
        RealMatrix Xt = X.transpose();
        RealMatrix XtX = Xt.multiply(X);

        // Add regularization term
        double lambda = 0.01;  // Regularization parameter
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(m + 1);
        XtX = XtX.add(identity.scalarMultiply(lambda));

        RealMatrix XtXInverse = new LUDecomposition(XtX).getSolver().getInverse();
        RealVector XtY = Xt.operate(Y);

        RealVector B = XtXInverse.operate(XtY);

        return B.toArray();
    }
}
