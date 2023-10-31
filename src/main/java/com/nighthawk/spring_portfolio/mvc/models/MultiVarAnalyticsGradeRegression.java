package com.nighthawk.spring_portfolio.mvc.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

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

public class MultiVarAnalyticsGradeRegression {

    public static void main(String[] args) {
        int numStudents = 100;
        double[][] xData = MockDataGenerator.generateXData(numStudents);
        double[] yData = MockDataGenerator.generateYData(xData);

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

    public static double[] getColumn(double[][] matrix, int columnIndex) {
        double[] column = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            column[i] = matrix[i][columnIndex];
        }
        return column;
    }

    public static double[] calculateCoefficients(double[][] xData, double[] yData) {
        int n = xData.length;
        int m = xData[0].length;
    
        // Calculate 10th and 75th percentiles for each predictor
        double[] q10s = new double[m];
        double[] q75s = new double[m];
        for (int j = 0; j < m; j++) {
            double[] column = getColumn(xData, j);
            q10s[j] = calculateQuantile(column, 0.15);
            q75s[j] = calculateQuantile(column, 0.85);
        }
    
        // Filter data to only include rows between 10th and 75th percentiles for all predictors
        List<double[]> filteredXList = new ArrayList<>();
        List<Double> filteredYList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            boolean include = true;
            for (int j = 0; j < m; j++) {
                if (xData[i][j] < q10s[j] || xData[i][j] > q75s[j]) {
                    include = false;
                    break;
                }
            }
            if (include) {
                filteredXList.add(xData[i]);
                filteredYList.add(yData[i]);
            }
        }
        double[][] filteredX = filteredXList.toArray(new double[0][0]);
        double[] filteredY = filteredYList.stream().mapToDouble(Double::doubleValue).toArray();
    
        // Now perform regression on filtered data
        n = filteredX.length;
        RealMatrix X = new Array2DRowRealMatrix(n, m + 1); // +1 for bias term
        RealVector Y = new ArrayRealVector(filteredY, false);
    
        for (int i = 0; i < n; i++) {
            X.setEntry(i, 0, 1);  // Bias term
            for (int j = 0; j < m; j++) {
                X.setEntry(i, j + 1, filteredX[i][j]);
            }
        }
    
        RealMatrix Xt = X.transpose();
        RealMatrix XtX = Xt.multiply(X);
        double lambda = 0.01;
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(m + 1);
        XtX = XtX.add(identity.scalarMultiply(lambda));
        RealMatrix XtXInverse = new LUDecomposition(XtX).getSolver().getInverse();
        RealVector XtY = Xt.operate(Y);
        RealVector B = XtXInverse.operate(XtY);
    
        return B.toArray();
    }        
    

    public static void displayChart(double[] xData, double[] yData, double[] coefficients, String metricName) {
        XYSeries series = new XYSeries("Students");
        XYSeries regressionLine = new XYSeries("Regression Line");
        XYSeries outliers = new XYSeries("Outliers");
    
        double q1 = calculateQuantile(xData, 0.25);
        double q3 = calculateQuantile(xData, 0.75);
        double iqr = q3 - q1;
        double lowerBound = q1 - 0.5 * iqr;
        double upperBound = q3 + 0.5 * iqr;
    
        for (int i = 0; i < xData.length; i++) {
            if (xData[i] < lowerBound || xData[i] > upperBound) {
                outliers.add(xData[i], yData[i]);
            } else {
                series.add(xData[i], yData[i]);
                double predictedY = coefficients[0] + coefficients[1] * xData[i];
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
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesPaint(2, Color.GREEN);
    
        chart.getXYPlot().setRenderer(renderer);
    
        try {
            BufferedImage chartImage = chart.createBufferedImage(800, 600);
            ImageIO.write(chartImage, "png", new java.io.File( metricName + ".png"));
            System.out.println("Chart saved as " + metricName + ".png");
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
