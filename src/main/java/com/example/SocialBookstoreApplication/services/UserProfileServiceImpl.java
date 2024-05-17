package com.example.SocialBookstoreApplication.services;

import com.example.SocialBookstoreApplication.domainmodel.*;
import com.example.SocialBookstoreApplication.domainmodel.strategies.*;
import com.example.SocialBookstoreApplication.formsdata.BookFormData;
import com.example.SocialBookstoreApplication.formsdata.RecommendationsFormData;
import com.example.SocialBookstoreApplication.formsdata.SearchFormData;
import com.example.SocialBookstoreApplication.formsdata.UserProfileFormData;
import com.example.SocialBookstoreApplication.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserProfileMapper userProfileMapper;
    @Autowired
    private BookAuthorMapper bookAuthorMapper;
    @Autowired
    private BookCategoryMapper bookCategoryMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private SearchFactory searchFactory;
    @Autowired
    private RecommendationsFactory recommendationsFactory;

    @Override
    public UserProfileFormData retrieveProfile(String username) {
        UserProfile userProfile = userProfileMapper.findByUsername(username);
        if (!userProfile.getUsername().equals(username)) {
            throw new IllegalArgumentException("User profile not found with username: " + username);
        }

        return userProfileMapper.toUserProfileFormData(userProfile);
    }

    @Override
    @Transactional
    public void save(UserProfileFormData userProfileFormData) {
        // Convert DTO to Entity
        UserProfile userProfile = userProfileMapper.toUserProfile(userProfileFormData);

        // Save the entity
        userProfileMapper.save(userProfile);
    }

    @Override
    public List<BookFormData> retrieveBookOffers(String username) {
        UserProfile userProfile = userProfileMapper.findByUsername(username);
        if (userProfile == null) {
            throw new RuntimeException("User profile not found for username: " + username);
        }
        return userProfile.getBookOffers().stream()
                .map(bookMapper::toBookFormData)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addBookOffer(String username, BookFormData bookFormData) {
        // Retrieve UserProfile by username
        UserProfile userProfile = userProfileMapper.findByUsername(username);
        if (userProfile == null) {
            throw new RuntimeException("User profile not found for username: " + username);
        }

        // Map BookFormData to Book
        Book book = bookMapper.toBook(bookFormData);

        // Retrieve or create authors and add them to the book
        List<BookAuthor> authors = new ArrayList<>();
        for (String authorName : bookFormData.getAuthorNames()) {
            List<BookAuthor> foundAuthors = bookAuthorMapper.findByName(authorName);
            if (foundAuthors.isEmpty()) {
                BookAuthor newAuthor = new BookAuthor();
                newAuthor.setName(authorName);
                bookAuthorMapper.save(newAuthor);
                authors.add(newAuthor);
            } else {
                authors.addAll(foundAuthors);
            }
        }
        book.setBookAuthors(authors);

        // Add the book to the user profile's list of book offers
        userProfile.getBookOffers().add(book);

        // Save the updated user profile
        userProfileMapper.save(userProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookFormData> searchBooks(SearchFormData searchFormData) {
        Specification<Book> spec = Specification.where(null);

        if (searchFormData.getTitle() != null) {
            spec = spec.and(BookSpecifications.hasTitle(searchFormData.getTitle()));
        }
        if (searchFormData.getAuthorName() != null) {
            spec = spec.and(BookSpecifications.hasAuthorName(searchFormData.getAuthorName()));
        }

        List<Book> results = bookMapper.findAll((Sort) spec);
        return results.stream().map(bookMapper::toBookFormData).collect(Collectors.toList());
    }

    @Override
    public List<BookFormData> recommendBooks(String username, RecommendationsFormData recomFormData) {
        UserProfile userProfile = userProfileMapper.findByUsername(username);
        RecommendationsStrategy recommendationsStrategy = recommendationsFactory.getStrategy(recomFormData.getStrategyType());
        List<Book> books = recommendationsStrategy.recommend(recomFormData, userProfile);
        return books.stream().map(bookMapper::toBookFormData).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void requestBook(int bookId, String username) {
        UserProfile userProfile = userProfileMapper.findByUsername(username);

        Book book = bookMapper.findByBookId(bookId);

        if (userProfile == null) {
            throw new RuntimeException("User profile not found with name: " + username);
        }
        if (book == null) {
            throw new RuntimeException("Book not found with ID: " + bookId);
        }

        // Ensure the book is not already requested by the user
        if (!userProfile.getRequestedBooks().contains(book)) {
            userProfile.getRequestedBooks().add(book); // Add the book to the user's requested books
            book.getRequestingUsers().add(userProfile); // Add the user to the book's list of requesting users
            userProfileMapper.save(userProfile); // Save changes
        } else {
            throw new IllegalStateException("Book already requested by this user");
        }
    }

    @Override
    public List<BookFormData> retrieveBookRequests(String username) {
        UserProfile user = userProfileMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found with username: " + username);

        // Convert each Book entity to BookFormData using MapStruct mapper
        return user.getRequestedBooks().stream()
                .map(bookMapper::toBookFormData)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfileFormData> retrieveRequestingUsers(int bookId) {
        Book book = bookMapper.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Convert each UserProfile entity to UserProfileFormData using MapStruct mapper
        return book.getRequestingUsers().stream()
                .map(userProfileMapper::toUserProfileFormData)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBookOffer(String username, int bookId) {
        UserProfile userProfile = userProfileMapper.findByUsername(username);
        Book bookToRemove = bookMapper.findByBookId(bookId);
        if (userProfile.getBookOffers().removeIf(book -> book.getBookId() == bookId)) {
            bookMapper.delete(bookToRemove); // Remove the book from the database if it's found in the user's list
        } else {
            throw new RuntimeException("Book not found in user's offers");
        }
    }

    @Override
    public void deleteBookRequest(String username, int bookId) {
        UserProfile user = userProfileMapper.findByUsername(username);

        Book book = bookMapper.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Check if the user actually requested the book and remove it
        boolean removed = user.getRequestedBooks().removeIf(b -> b.getBookId() == bookId);
        if (removed) {
            // Optionally, remove the user from the book's list of requesting users
            book.getRequestingUsers().remove(user);
            userProfileMapper.save(user); // Persist changes
        } else {
            throw new RuntimeException("Book request not found in user's requested books");
        }
    }
}
