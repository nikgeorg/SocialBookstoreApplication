package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.mappers.BookMapper;
import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.domainmodel.BookAuthor;
import com.example.SocialBookstoreApplication.domainmodel.UserProfile;
import com.example.SocialBookstoreApplication.formsdata.RecommendationsFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorRecommendationsStrategy implements RecommendationsStrategy {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> recommend(RecommendationsFormData recommendationsFormData, UserProfile userProfile) {
        List<String> favoriteAuthors = userProfile.getFavouriteBookAuthors().stream()
                .map(BookAuthor::getName)
                .toList();

        return bookMapper.findAll().stream()
                .filter(book -> book.getBookAuthors().stream()
                        .anyMatch(author -> favoriteAuthors.contains(author.getName())))
                .collect(Collectors.toList());
    }
}




