package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.mappers.BookCategoryMapper;
import com.example.SocialBookstoreApplication.mappers.BookMapper;
import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.domainmodel.BookCategory;
import com.example.SocialBookstoreApplication.domainmodel.UserProfile;
import com.example.SocialBookstoreApplication.formsdata.RecommendationsFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class TemplateRecommendationsStrategy implements RecommendationsStrategy {

    @Autowired
    protected BookCategoryMapper bookCategoryMapper;

    @Autowired
    protected BookMapper bookMapper;

    @Override
    public abstract List<Book> recommend(RecommendationsFormData recommendationsFormData, UserProfile userProfile);
}


