package com.nighthawk.spring_portfolio.mvc.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

        // Construct the linear computation string
        double[] coeffs = result.getCoefficients();
        String computation = String.format(
            "%.2f*Commits + %.2f*Pulls + %.2f*Issues + %.2f*ReposContributedTo + %.2f = Predicted Score",
            coeffs[1], coeffs[2], coeffs[3], coeffs[4], coeffs[0]
        );

        // Generate charts for each metric
        String[] metrics = {"Commits", "Pulls", "Issues", "ReposContributedTo"};
        List<String> imageUrls = new ArrayList<>();
        for (String metric : metrics) {
            MultiVarAnalyticsGradeRegression.displayChart(result.getXData()[0], result.getYData(), coeffs, metric); // Assuming XData[0] corresponds to the first metric
            imageUrls.add("/images/" + metric + ".png");
        }

        GradePredictionResponse response = new GradePredictionResponse();
        response.setComputation(computation);
        response.setImageUrls(imageUrls);

        return ResponseEntity.ok(response);
    }

    static class GradePredictionRequest {
        private int numStudents;

        public int getNumStudents() {
            return numStudents;
        }

        public void setNumStudents(int numStudents) {
            this.numStudents = numStudents;
        }
    }

    static class GradePredictionResponse {
        private String computation;
        private List<String> imageUrls; // URLs to the generated chart images

        public String getComputation() {
            return computation;
        }

        public void setComputation(String computation) {
            this.computation = computation;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }
}