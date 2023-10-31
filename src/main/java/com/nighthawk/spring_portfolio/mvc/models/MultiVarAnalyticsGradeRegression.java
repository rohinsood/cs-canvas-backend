package com.nighthawk.spring_portfolio.mvc.models;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MultiVarAnalyticsGradeRegression {
    public static void main(String[] args) {
        // Mock data representing GitHub analytics for each student
        // Generate mock data representing GitHub analytics for 100 students
        int numStudents = 100;
        double[][] xData = MockDataGenerator.generateXData(numStudents);
        double[] yData = MockDataGenerator.generateYData(xData);

        // Calculate coefficients
        double[] coefficients = calculateCoefficients(xData, yData);
        double[] coefficientsCommits = {coefficients[0], coefficients[1]};
        double[] coefficientsPulls = {coefficients[0], coefficients[2]};
        double[] coefficientsIssues = {coefficients[0], coefficients[3]};
        double[] coefficientsRepos = {coefficients[0], coefficients[4]};

        System.out.println("Coefficients: " + Arrays.toString(coefficients));

        displayChart(getColumn(xData, 0), yData, coefficientsCommits, "Commits");
        displayChart(getColumn(xData, 1), yData, coefficientsPulls, "Pulls");
        displayChart(getColumn(xData, 2), yData, coefficientsIssues, "Issues");
        displayChart(getColumn(xData, 3), yData, coefficientsRepos, "ReposContributedTo");
    }

    public static double[][] getColumn(double[][] matrix, int columnIndex) {
        double[][] column = new double[matrix.length][1];
        for (int i = 0; i < matrix.length; i++) {
            column[i][0] = matrix[i][columnIndex];
        }
        return column;
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

    public static void displayChart(double[][] xData, double[] yData, double[] coefficients, String metricName) {
        XYSeries series = new XYSeries("Students");
        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i][0], yData[i]);
        }

        XYSeries regressionLine = new XYSeries("Regression Line");
        for (int i = 0; i < xData.length; i++) {
            double predictedY = coefficients[0] + coefficients[1] * xData[i][0];
            regressionLine.add(xData[i][0], predictedY);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(regressionLine);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Grades vs " + metricName,
                metricName,
                "Grades",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        try {
            BufferedImage chartImage = chart.createBufferedImage(800, 600);
            ImageIO.write(chartImage, "png", new java.io.File("src/main/resources/static/images/" + metricName + ".png"));
            System.out.println("Chart saved as " + metricName + ".png");
        } catch (java.io.IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }
}
