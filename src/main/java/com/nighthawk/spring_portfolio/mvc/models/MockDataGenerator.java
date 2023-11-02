package com.nighthawk.spring_portfolio.mvc.models;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class MockDataGenerator {

    private static final Random random = new Random();

    public static double[][] generateXData(int numStudents) {
        double[][] xData = new double[numStudents][4];
    
        for (int i = 0; i < numStudents; i++) {
            int commits = 30 + random.nextInt(170); // Uniform distribution between 30 and 200
            int pullRequests = 5 + random.nextInt(43); // Uniform distribution between 5 and 48
            int issues = 10 + random.nextInt(91); // Uniform distribution between 10 and 101
            int reposContributed = 2 + random.nextInt(19); // Uniform distribution between 2 and 21
    
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
        double commitGrade = (commits <= 40) ? 60 + (commits - 30) * 1 : Math.min(100, 90 + (10 * (1 - 1 / Math.log(commits - 29))));
        double pullRequestGrade = (pullRequests <= 10) ? 60 + (pullRequests - 5) * 3 : Math.min(100, 90 + (10 * (1 - 1 / Math.log(pullRequests - 4))));
        double issueGrade = (issues <= 30) ? 60 + (issues - 10) * 1.33 : Math.min(100, 90 + (10 * (1 - 1 / Math.log(issues - 9))));
        double repoGrade = (reposContributed <= 7) ? 60 + (reposContributed - 2) * 5 : Math.min(100, 90 + (10 * (1 - 1 / Math.log(reposContributed - 1))));

        return 0.4 * commitGrade + 0.2 * pullRequestGrade + 0.2 * issueGrade + 0.2 * repoGrade;
    }
}