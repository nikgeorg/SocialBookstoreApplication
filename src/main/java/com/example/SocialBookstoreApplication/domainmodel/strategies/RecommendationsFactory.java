package com.example.SocialBookstoreApplication.domainmodel.strategies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecommendationsFactory {

    @Autowired
    private CategoryRecommendationsStrategy categoryRecommendationsStrategy;

    @Autowired
    private AuthorRecommendationsStrategy authorRecommendationsStrategy;

    public RecommendationsStrategy getStrategy(String strategyType) {
        return switch (strategyType) {
            case "Category" -> categoryRecommendationsStrategy;
            case "Author" -> authorRecommendationsStrategy;
            default -> throw new IllegalArgumentException("Invalid strategy type");
        };
    }
}




