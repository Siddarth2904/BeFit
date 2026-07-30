package com.fitness.aiservice.services;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepository recommendationRepository;
    public List<Recommendation> getRecommendationsByUserId(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Recommendation getRecommendationsByActivityId(String activityId) {
        return recommendationRepository.findByActivityId(activityId)
                .orElseThrow(()->new RuntimeException("Recommendation not found for "+activityId));
    }
}
