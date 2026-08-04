package com.fitness.aiservice.services;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        String prompt=createPromptForActivity(activity);
        String aiResponse=geminiService.getAnswer(prompt);
        log.info("Recommendation for activity: " + activity.getId());
        Recommendation recommendation=processAIResponse(activity,aiResponse);
        return recommendation;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
                {
                "analysis":{
                    "overall":"overall analysys here",
                    "pace":"Pace analysis here",
                    "heartRate":"Heart rate analysis here",
                    "caloriesBurned":"Calories analysis here"
                    },
                    "improvements":[{
                        "area":"area name",
                        "recommendation":"Detailed recommendation"
                    }],
                    "suggestions":[{
                        "workout":"Workout name",
                        "description":"detailed workout description"
                    }],
                    "safety":["Safety point 1","Safety point 2"]
                  }
                  Analyze this activity:
                  Activity Type:%s
                  Duration:%d minutes
                  Calories Burned:%d
                  Additional Metrics:%s
                  Provide detailed analysis focusing on performance, improvements,next workout suggestions, and safety guidelines
                  Ensure the reponse follows the EXACT JSON format shown above.
                """,activity.getType(),
                    activity.getDuration(),
                    activity.getCaloriesBurned(),
                    activity.getAdditionalMetrics()
        );
    }
    private Recommendation processAIResponse(Activity activity,String aiResponse){
        try{
            ObjectMapper om=new ObjectMapper();
            JsonNode rootNode=om.readTree(aiResponse);
            JsonNode textNode=rootNode.path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text");

            String jsonContent=textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```","")
                    .trim();

            log.info("Processed response: " + jsonContent);
            JsonNode analysisJson=om.readTree(jsonContent);
            JsonNode analysisNode=analysisJson.path("analysis");
            StringBuilder fullAnalysis=new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall:");
            addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace:");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate","Heart Rate:");
            addAnalysisSection(fullAnalysis,analysisNode,"caloriesBurned","Calories Burned:");
            List<String> improvements=extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions=extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety=extractSafety(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(String.valueOf(activity.getType()))
                    .recommendation(fullAnalysis.toString().trim())
                    .impovements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        }catch(Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(String.valueOf(activity.getType()))
                .recommendation("Unable to generate detailed analysis")
                .impovements(Collections.singletonList("Continue with your current routine"))
                .suggestions(Collections.singletonList("Consider consulting a fitness coach"))
                .safety(Arrays.asList("always warm up before exercise","Stay Hydrated","Listen to your body"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safeties=new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item->safeties.add(item.asText()));
        }
        return  safeties.isEmpty() ?
                Collections.singletonList("No specfic improvements provided") : safeties;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions=new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion->{
                String workout=suggestion.path("workout").asText();
                String description=suggestion.path("description").asText();
                suggestions.add(String.format("%s,%s",workout,description));
            });
        }
        return  suggestions.isEmpty() ?
                Collections.singletonList("No specfic improvements provided") : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements=new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement->{
                String area=improvement.path("area").asText();
                String details=improvement.path("recommendation").asText();
                improvements.add(String.format("%s,%s",area,details));
            });
        }
        return  improvements.isEmpty() ?
                Collections.singletonList("No specfic improvements provided") : improvements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }
}
