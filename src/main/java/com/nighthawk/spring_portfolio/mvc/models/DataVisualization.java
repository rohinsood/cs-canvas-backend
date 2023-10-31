package com.nighthawk.spring_portfolio.mvc.models;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DataVisualization {
    public JFreeChart createChart(double[][] xData, double[] yData, String xAxisLabel, String yAxisLabel, String title) {
        XYSeries series = new XYSeries(title);
        for (int i = 0; i < xData.length; i++) {
            series.add(xData[i][0], yData[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            xAxisLabel,
            yAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        return chart;
    }
}
