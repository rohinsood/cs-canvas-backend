package com.nighthawk.spring_portfolio.mvc.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/grade")

public class GradePredictionController {

    @Autowired
    private MultiVarAnalyticsGradeRegression regressionService;

    @PostMapping("/predict")
    public ResponseEntity<GradePredictionResponse> predictGrade(@RequestBody GradePredictionRequest request) {
        MultiVarAnalyticsGradeRegression.RegressionResult result = regressionService.performRegression(request.getNumStudents());

        // Extract coefficients
        double[] coeffs = result.getCoefficients();

        // Generate charts for each metric and collect image URLs
        String[] metrics = {"Commits", "Pulls", "Issues", "ReposContributedTo"};
        List<String> imageUrls = new ArrayList<>();
        for (String metric : metrics) {
            MultiVarAnalyticsGradeRegression.displayChart(result.getXData()[0], result.getYData(), coeffs, metric); // Assuming XData[0] corresponds to the first metric
            imageUrls.add("/images/" + metric + ".png");
        }

        // Construct the response
        GradePredictionResponse response = new GradePredictionResponse();
        response.setBias(coeffs[0]);
        response.setCommitCoefficient(coeffs[1]);
        response.setPullCoefficient(coeffs[2]);
        response.setIssueCoefficient(coeffs[3]);
        response.setReposContributedToCoefficient(coeffs[4]);
        response.setImageUrls(imageUrls);

        return ResponseEntity.ok(response);
    }

    static class GradePredictionRequest {
        private final int numStudents = 1000; // Initialize with 250 and make it final
    
        // Only Getter for numStudents (no setter since we don't want it to be changed)
        public int getNumStudents() {
            return numStudents;
        }
    }
    

    static class GradePredictionResponse {
        private double bias;
        private double commitCoefficient;
        private double pullCoefficient;
        private double issueCoefficient;
        private double reposContributedToCoefficient;
        private List<String> imageUrls; // URLs to the generated chart images
    
        public double getBias() {
            return bias;
        }
    
        public void setBias(double bias) {
            this.bias = bias;
        }

        public double getCommitCoefficient() {
            return commitCoefficient;
        }
    
        public void setCommitCoefficient(double commitCoefficient) {
            this.commitCoefficient = commitCoefficient;
        }
    
        public double getPullCoefficient() {
            return pullCoefficient;
        }
    
        public void setPullCoefficient(double pullCoefficient) {
            this.pullCoefficient = pullCoefficient;
        }
    
        public double getIssueCoefficient() {
            return issueCoefficient;
        }
    
        public void setIssueCoefficient(double issueCoefficient) {
            this.issueCoefficient = issueCoefficient;
        }
    
        public double getReposContributedToCoefficient() {
            return reposContributedToCoefficient;
        }
    
        public void setReposContributedToCoefficient(double reposContributedToCoefficient) {
            this.reposContributedToCoefficient = reposContributedToCoefficient;
        }
    
        public List<String> getImageUrls() {
            return imageUrls;
        }
    
        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }    
}