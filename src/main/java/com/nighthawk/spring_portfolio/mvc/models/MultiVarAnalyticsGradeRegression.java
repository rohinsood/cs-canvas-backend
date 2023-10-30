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

    
}
