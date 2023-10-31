package com.nighthawk.spring_portfolio.mvc.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MultiVarLogarithmicRegression {

    public static void main(String[] args) {
        int numStudents = 100;
        double[][] xData = MockDataGenerator.generateXData(numStudents);
        double[] yData = MockDataGenerator.generateYData(xData);

        double[] coefficients = calculateCoefficients(xData, yData);

        System.out.println("Coefficients: " + Arrays.toString(coefficients));

        displayChart(getColumn(xData, 0), yData, coefficients, "Commits");
        displayChart(getColumn(xData, 1), yData, coefficients, "Pulls");
        displayChart(getColumn(xData, 2), yData, coefficients, "Issues");
        displayChart(getColumn(xData, 3), yData, coefficients, "ReposContributedTo");
    }

    public static double[] getColumn(double[][] matrix, int columnIndex) {
        double[] column = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][columnIndex];
        }
        return column;
    }

    public static double[] gradientDescent(double[][] X, double[] y, double alpha, int iterations) {
        int m = y.length;
        int n = X[0].length;
        double[] theta = new double[n];
        double[] temp = new double[n];
    
        for (int iter = 0; iter < iterations; iter++) {
            for (int j = 0; j < n; j++) {
                double sum = 0;
                for (int i = 0; i < m; i++) {
                    double hypothesis = 0;
                    for (int k = 0; k < n; k++) {
                        hypothesis += theta[k] * X[i][k];
                    }
                    sum += (hypothesis - y[i]) * X[i][j];
                }
                temp[j] = theta[j] - alpha * sum / m;
            }
            System.arraycopy(temp, 0, theta, 0, n);
        }
    
        return theta;
    }

    public static double[] calculateCoefficients(double[][] xData, double[] yData) {
        // Log transform the predictors
        for (int i = 0; i < xData.length; i++) {
            for (int j = 0; j < xData[i].length; j++) {
                xData[i][j] = Math.log(xData[i][j] + 1); // +1 to avoid log(0)
            }
        }

        // Control for outliers using Interquartile method
        double q1 = calculateQuantile(getColumn(xData, 0), 0.25);
        double q3 = calculateQuantile(getColumn(xData, 0), 0.75);
        double iqr = q3 - q1;
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        List<double[]> filteredXDataList = new ArrayList<>();
        List<Double> filteredYDataList = new ArrayList<>();
        for (int i = 0; i < xData.length; i++) {
            if (xData[i][0] >= lowerBound && xData[i][0] <= upperBound) {
                filteredXDataList.add(xData[i]);
                filteredYDataList.add(yData[i]);
            }
        }
        double[][] filteredXData = filteredXDataList.toArray(new double[0][0]);
        double[] filteredYData = filteredYDataList.stream().mapToDouble(Double::doubleValue).toArray();

        int n = filteredXData.length;
        int m = filteredXData[0].length;

        double[][] X = new double[n][m + 1];
        for (int i = 0; i < n; i++) {
            X[i][0] = 1;  // Bias term
            for (int j = 0; j < m; j++) {
                X[i][j + 1] = filteredXData[i][j];
            }
        }

        double alpha = 0.01;  // Learning rate
        int iterations = 1000;
        double[] coefficients = gradientDescent(X, filteredYData, alpha, iterations);

        return coefficients;
    }

    public static void displayChart(double[] xData, double[] yData, double[] coefficients, String metricName) {
    XYSeries series = new XYSeries("Students");
    XYSeries regressionLine = new XYSeries("Regression Line");
    XYSeries outliers = new XYSeries("Outliers");

    double q1 = calculateQuantile(xData, 0.25);
    double q3 = calculateQuantile(xData, 0.75);
    double iqr = q3 - q1;
    double lowerBound = q1 - 1.5 * iqr;
    double upperBound = q3 + 1.5 * iqr;

    for (int i = 0; i < xData.length; i++) {
        if (xData[i] < lowerBound || xData[i] > upperBound) {
            outliers.add(xData[i], yData[i]);
        } else {
            series.add(xData[i], yData[i]);
            double predictedY = coefficients[0];
            for (int j = 1; j < coefficients.length; j++) {
                predictedY += coefficients[j] * Math.log(xData[i] + 1); // +1 to avoid log(0)
            }
            regressionLine.add(xData[i], predictedY);
        }
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);
    dataset.addSeries(regressionLine);
    dataset.addSeries(outliers);

    JFreeChart chart = ChartFactory.createScatterPlot(
            "Grades vs " + metricName,
            metricName,
            "Grades",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
    );

    XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
    renderer.setSeriesLinesVisible(0, false);  // No line for student data points
    renderer.setSeriesShapesVisible(0, true);  // Dots for student data points
    renderer.setSeriesPaint(0, Color.RED);     // Red color for student data points

    renderer.setSeriesLinesVisible(1, true);   // Line for regression
    renderer.setSeriesShapesVisible(1, false); // No dots for regression
    renderer.setSeriesPaint(1, Color.BLUE);    // Blue color for regression line

    renderer.setSeriesShapesVisible(2, true);  // Dots for outliers
    renderer.setSeriesLinesVisible(2, false);  // No line for outliers
    renderer.setSeriesPaint(2, Color.GREEN);   // Green color for outliers

    chart.getXYPlot().setRenderer(renderer);

    try {
        BufferedImage chartImage = chart.createBufferedImage(800, 600);
        ImageIO.write(chartImage, "png", new java.io.File("src/main/resources/static/images/" + metricName + "Log.png"));
        System.out.println("Chart saved as " + metricName + "log.png");
    } catch (java.io.IOException e) {
        System.err.println("Problem occurred creating chart.");
    }
}


    public static double calculateQuantile(double[] data, double quantile) {
        double[] sortedData = data.clone();
        Arrays.sort(sortedData);
        int n = sortedData.length;
    
        if (quantile < 0 || quantile > 1) {
            throw new IllegalArgumentException("Quantile should be between 0 and 1");
        }
    
        double pos = (n - 1) * quantile;
        int lower = (int) Math.floor(pos);
        int upper = (int) Math.ceil(pos);
        if (lower == upper) {
            return sortedData[lower];
        }
        double weight = pos - lower;
        return (1 - weight) * sortedData[lower] + weight * sortedData[upper];
    }    
    
}
