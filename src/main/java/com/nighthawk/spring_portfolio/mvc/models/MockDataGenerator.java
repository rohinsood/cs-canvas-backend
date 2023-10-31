package com.nighthawk.spring_portfolio.mvc.models;

import java.util.Random;

public class MockDataGenerator {

    private static final Random random = new Random();

    public static double[][] generateXData(int numStudents) {
        double[][] xData = new double[numStudents][4];

        for (int i = 0; i < numStudents; i++) {
            int commits = 30 + random.nextInt(171); // 30 to 200
            int pullRequests = 5 + random.nextInt(46); // 5 to 50
            int issues = 10 + random.nextInt(91); // 10 to 100
            int reposContributed = 2 + random.nextInt(19); // 2 to 20

            xData[i][0] = commits;
            xData[i][1] = pullRequests;
            xData[i][2] = issues;
            xData[i][3] = reposContributed;
        }

        return xData;
    }

    
}