package com.fitness.aiservice.contollers;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByUserId(userId));
    }
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getRecommendationsByActivityId(@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByActivityId(activityId));
    }
}
