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
import org.springframework.stereotype.Component;

@Component
public class MultiVarAnalyticsGradeRegression {

    public void performCrossValidation(int numStudents) {
        double[][] xData = MockDataGenerator.generateXData(numStudents);
        double[][] normalizedXData = normalize(xData);
        double[] yData = MockDataGenerator.generateYData(xData);

        // Split the data into training and testing sets (80-20 split)
        int trainSize = (int) (0.8 * numStudents);
        double[][] trainX = Arrays.copyOfRange(normalizedXData, 0, trainSize);
        double[][] testX = Arrays.copyOfRange(normalizedXData, trainSize, numStudents);
        double[] trainY = Arrays.copyOfRange(yData, 0, trainSize);
        double[] testY = Arrays.copyOfRange(yData, trainSize, numStudents);

        // Train the regression model on the training set
        double[] coefficients = calculateCoefficients(trainX, trainY);

        // Predict the grades for the testing set
        double[] predictedY = new double[testY.length];
        for (int i = 0; i < testX.length; i++) {
            predictedY[i] = coefficients[0];  // bias term
            for (int j = 0; j < testX[i].length; j++) {
                predictedY[i] += coefficients[j + 1] * testX[i][j];
            }
        }

        // Calculate the Mean Squared Error (MSE) for the testing set
        double mse = 0;
        for (int i = 0; i < testY.length; i++) {
            mse += Math.pow(testY[i] - predictedY[i], 2);
        }
        mse /= testY.length;

        System.out.println("Mean Squared Error on Testing Set: " + mse);
    }

    // Modify the main method to call performCrossValidation instead of performRegression
    public static void main(String[] args) {
        MultiVarAnalyticsGradeRegression regression = new MultiVarAnalyticsGradeRegression();
        int numStudents = 1000;
        regression.performCrossValidation(numStudents);
    }

    

    public static double[][] normalize(double[][] xData) {
        int n = xData.length;
        int m = xData[0].length;
        double[][] normalizedData = new double[n][m];

        for (int j = 0; j < m; j++) {
            double[] column = getColumn(xData, j);
            double mean = Arrays.stream(column).average().orElse(0);
            double stddev = Math.sqrt(Arrays.stream(column).map(val -> Math.pow(val - mean, 2)).average().orElse(0));

            for (int i = 0; i < n; i++) {
                normalizedData[i][j] = (xData[i][j] - mean) / stddev;
            }
        }

        return normalizedData;
    }

    public RegressionResult performRegression(int numStudents) {
        double[][] xData = MockDataGenerator.generateXData(numStudents);
        double[][] normalizedXData = normalize(xData);  // Normalize the xData
        double[] yData = MockDataGenerator.generateYData(xData);

        double[] coefficients = calculateCoefficients(normalizedXData, yData);  // Use normalized xData for regression

        RegressionResult result = new RegressionResult();
        result.setCoefficients(coefficients);
        result.setXData(xData);  // Store non-normalized xData in the result for graphing
        result.setYData(yData);

        return result;
    }

    public static class RegressionResult {
        private double[][] xData;
        private double[] yData;
        private double[] coefficients;

        // Getters and setters...
        public double[][] getXData() {
            return xData;
        }

        public void setXData(double[][] xData) {
            this.xData = xData;
        }

        public double[] getYData() {
            return yData;
        }

        public void setYData(double[] yData) {
            this.yData = yData;
        }

        public double[] getCoefficients() {
            return coefficients;
        }

        public void setCoefficients(double[] coefficients) {
            this.coefficients = coefficients;
        }
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
    
        // Calculate 15th and 85th percentiles for each predictor
        double[] q15s = new double[m];
        double[] q85s = new double[m];
        for (int j = 0; j < m; j++) {
            double[] column = getColumn(xData, j);
            q15s[j] = calculateQuantile(column, 0.15);
            q85s[j] = calculateQuantile(column, 0.85);
        }
    
        // Filter data to only include rows between 15th and 85th percentiles for all predictors
        List<double[]> filteredXList = new ArrayList<>();
        List<Double> filteredYList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            boolean include = true;
            for (int j = 0; j < m; j++) {
                if (xData[i][j] < q15s[j] || xData[i][j] > q85s[j]) {
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
    
        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i], yData[i]);
        }
    
        // Plotting the regression line across a range of x-values
        double minX = Arrays.stream(xData).min().orElse(0);
        double maxX = Arrays.stream(xData).max().orElse(1);
        for (double x = minX; x <= maxX; x += (maxX - minX) / 100.0) {  // 100 points for the regression line
            double predictedY = coefficients[0] + coefficients[1] * x;
            regressionLine.add(x, predictedY);
        }
    
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        dataset.addSeries(regressionLine);
    
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
            ImageIO.write(chartImage, "png", new java.io.File("src/main/resources/static/images/" + metricName + ".png"));
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
