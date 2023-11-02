package com.nighthawk.spring_portfolio.mvc.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @GetMapping("/predict")
    public ResponseEntity<GradePredictionResponse> getPredictionDetails() {
        // For demonstration purposes, returning a sample response. 
        // You can modify this to return actual data as needed.
        GradePredictionResponse response = new GradePredictionResponse();
        response.setCommitCoefficient(0.5); // Sample coefficient
        response.setPullCoefficient(0.4);   // Sample coefficient
        response.setIssueCoefficient(0.3);  // Sample coefficient
        response.setReposContributedToCoefficient(0.2); // Sample coefficient
        response.setImageUrls(List.of("SampleImageUrl1", "SampleImageUrl2"));
        return ResponseEntity.ok(response);
    }

    static class GradePredictionRequest {
        private final int numStudents = 1000; // Initialize with 250 and make it final
        private int commits;
        private int pulls;
        private int issues;
        private int repos;
    
        // Only Getter for numStudents (no setter since we don't want it to be changed)
        public int getNumStudents() {
            return numStudents;
        }
    
        // Getter and Setter for commits
        public int getCommits() {
            return commits;
        }
    
        public void setCommits(int commits) {
            this.commits = commits;
        }
    
        // Getter and Setter for pulls
        public int getPulls() {
            return pulls;
        }
    
        public void setPulls(int pulls) {
            this.pulls = pulls;
        }
    
        // Getter and Setter for issues
        public int getIssues() {
            return issues;
        }
    
        public void setIssues(int issues) {
            this.issues = issues;
        }
    
        // Getter and Setter for repos
        public int getRepos() {
            return repos;
        }
    
        public void setRepos(int repos) {
            this.repos = repos;
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