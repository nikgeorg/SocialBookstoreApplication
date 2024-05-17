package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.domainmodel.UserProfile;
import com.example.SocialBookstoreApplication.formsdata.RecommendationsFormData;

import java.util.List;

public interface RecommendationsStrategy {
    List<Book> recommend(RecommendationsFormData recommendationsFormData, UserProfile userProfile);
}



