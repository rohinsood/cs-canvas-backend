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

    public static double calculateSlope(ArrayList<Double> commits, ArrayList<Double> grades) {
        int n = commits.size();
        double sumCommits = 0, sumGrades = 0, sumCommitsGrades = 0, sumCommits2 = 0;

        for (int i = 0; i < n; i++) {
            sumCommits += commits.get(i);
            sumGrades += grades.get(i);
            sumCommitsGrades += commits.get(i) * grades.get(i);
            sumCommits2 += commits.get(i) * commits.get(i);
        }

        return (n * sumCommitsGrades - sumCommits * sumGrades) / (n * sumCommits2 - sumCommits * sumCommits);
    }

    public static double calculateIntercept(ArrayList<Double> commits, ArrayList<Double> grades, double m) {
        int n = commits.size();
        double sumGrades = 0, sumCommits = 0;

        for (int i = 0; i < n; i++) {
            sumGrades += grades.get(i);
            sumCommits += commits.get(i);
        }

        return (sumGrades - m * sumCommits) / n;
    }
}