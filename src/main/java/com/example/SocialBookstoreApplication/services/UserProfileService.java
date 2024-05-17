package com.example.SocialBookstoreApplication.services;

import com.example.SocialBookstoreApplication.formsdata.BookFormData;
import com.example.SocialBookstoreApplication.formsdata.RecommendationsFormData;
import com.example.SocialBookstoreApplication.formsdata.SearchFormData;
import com.example.SocialBookstoreApplication.formsdata.UserProfileFormData;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserProfileService {
    UserProfileFormData retrieveProfile(String username);
    void save(UserProfileFormData userProfileFormData);
    List<BookFormData> retrieveBookOffers(String username);
    void addBookOffer(String username, BookFormData bookFormData);
    List<BookFormData> searchBooks(SearchFormData searchFormData);
    List<BookFormData> recommendBooks(String username, RecommendationsFormData recomFormData);
    void requestBook(int bookId, String username);

    List<BookFormData> retrieveBookRequests(String username);
    List<UserProfileFormData> retrieveRequestingUsers(int bookId);
    void deleteBookOffer(String username, int bookId);
    void deleteBookRequest(String username, int bookId);
}
