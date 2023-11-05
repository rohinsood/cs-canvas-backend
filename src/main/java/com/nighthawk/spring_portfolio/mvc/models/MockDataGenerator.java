package com.nighthawk.spring_portfolio.mvc.models;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class MockDataGenerator {

    private static final Random random = new Random();

    public static double[][] generateXData(int numStudents) {
        double[][] xData = new double[numStudents][4];
    
        for (int i = 0; i < numStudents; i++) {
            int commits = 30 + random.nextInt(120);
            int pullRequests = 10 + random.nextInt(50);  // Generate independently
            int issues = 5 + random.nextInt(40);         // Generate independently
            int reposContributed = 2 + random.nextInt(20); // Generate independently           

    
            xData[i][0] = commits;
            xData[i][1] = pullRequests;
            xData[i][2] = issues;
            xData[i][3] = reposContributed;
        }
    
        return xData;
    }

    public static double[] generateYData(double[][] xData) {
        double[] yData = new double[xData.length];

        for (int i = 0; i < xData.length; i++) {
            yData[i] = calculateGrade((int)xData[i][0], (int)xData[i][1], (int)xData[i][2], (int)xData[i][3]);
        }

        return yData;
    }

    private static double calculateGrade(int commits, int pullRequests, int issues, int reposContributed) {
        double commitGrade;
        if (commits <= 30) {
            commitGrade = 60;
        } else {
            commitGrade = Math.min(100, 60 + (10 * (1 - 1 / Math.log(commits - 29))));
        }
    
        double pullRequestGrade;
        if (pullRequests <= 10) {
            pullRequestGrade = 60;
        } else {
            pullRequestGrade = Math.min(100, 60 + (10 * (1 - 1 / Math.log(pullRequests - 9))));
        }
    
        double issueGrade;
        if (issues <= 5) {
            issueGrade = 60;
        } else {
            issueGrade = Math.min(100, 60 + (10 * (1 - 1 / Math.log(issues - 4))));
        }
    
        double repoGrade;
        if (reposContributed <= 2) {
            repoGrade = 60;
        } else {
            repoGrade = Math.min(100, 60 + (10 * (1 - 1 / Math.log(reposContributed - 1))));
        }
    
        return 0.4 * commitGrade + 0.2 * pullRequestGrade + 0.2 * issueGrade + 0.2 * repoGrade;
    }
}