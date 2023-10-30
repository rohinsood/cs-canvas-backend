package com.nighthawk.spring_portfolio.mvc.models;

import java.util.ArrayList;
import java.util.Arrays;

public class SingleVarCommitsGradeRegression {

    public static void main(String[] args) {
        // Mock data
        ArrayList<Double> commitsData = new ArrayList<>(Arrays.asList(5.0, 10.0, 15.0, 20.0, 25.0)); // Number of commits
        ArrayList<Double> gradeData = new ArrayList<>(Arrays.asList(70.0, 75.0, 80.0, 85.0, 90.0)); // Corresponding grades

        double m = calculateSlope(commitsData, gradeData);
        double c = calculateIntercept(commitsData, gradeData, m);

        System.out.println("Linear Regression Model for Grade based on Commits: Grade = " + m + " * Commits + " + c);
    }

    
}