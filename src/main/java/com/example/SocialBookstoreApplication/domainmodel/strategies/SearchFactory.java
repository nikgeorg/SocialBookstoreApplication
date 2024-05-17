package com.example.SocialBookstoreApplication.domainmodel.strategies;

import org.springframework.stereotype.Component;

@Component
public class SearchFactory {

    public SearchFactory(){

    }

    public SearchStrategy getStrategy(String strategy){
        if (strategy.equals("ExactSearch")){
            return new ExactSearchStrategy();
        }
        else if (strategy.equals("ApproximateSearch")){
            return new ApproximateSearchStrategy();
        }
        else{
            return null;
        }
    }

}
