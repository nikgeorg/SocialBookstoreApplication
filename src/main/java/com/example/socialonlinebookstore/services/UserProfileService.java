package com.example.socialonlinebookstore.services;

import com.example.socialonlinebookstore.domainmodel.Book;
import com.example.socialonlinebookstore.domainmodel.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface UserProfileService {

    /**
     * Creates or updates a user profile.
     * @param username The username of the user whose profile is being created/updated.
     * @param fullName The full name of the user.
     * @param age The age of the user.
     */
    @Transactional
    void createProfile(String username, String fullName, int age, String address, String phoneNumber,
                       String favoriteBookAuthors, String favoriteBookCategories);
    UserProfile getUserProfile(String username);

    List<Book> retrieveBookOffers(String username);

    void addBookOffer(String username, Book book);

    void requestBook(int bookId, String username);

    List<Book> retrieveBookRequests(String username);
    List<UserProfile> retrieveRequestingUsers(int bookId);
    void removeBookOffer(int bookId, String username);
    List<Book> recommendBooks(String username, String recommendationStrategy);
}
