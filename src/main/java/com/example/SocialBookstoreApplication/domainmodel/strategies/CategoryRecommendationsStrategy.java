package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.mappers.BookMapper;
import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.domainmodel.BookCategory;
import com.example.SocialBookstoreApplication.domainmodel.UserProfile;
import com.example.SocialBookstoreApplication.formsdata.RecommendationsFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryRecommendationsStrategy implements RecommendationsStrategy {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> recommend(RecommendationsFormData recommendationsFormData, UserProfile userProfile) {
        List<String> favoriteCategories = userProfile.getFavouriteBookCategories().stream()
                .map(BookCategory::getName)
                .toList();

        return bookMapper.findAll().stream()
                .filter(book -> favoriteCategories.contains(book.getBookCategory().getName()))
                .collect(Collectors.toList());
    }
}




