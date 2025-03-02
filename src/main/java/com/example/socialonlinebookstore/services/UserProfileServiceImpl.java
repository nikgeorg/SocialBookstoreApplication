package com.example.socialonlinebookstore.services;

import com.example.socialonlinebookstore.domainmodel.*;
import com.example.socialonlinebookstore.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileMapper userProfileRepository;

    @Autowired
    private UserMapper userRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Override
    @Transactional
    public void createProfile(String username, String fullName, int age, String address, String phoneNumber,
                              String favoriteBookAuthors, String favoriteBookCategories) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        UserProfile profile = new UserProfile(user.getUsername(), fullName, age, address, phoneNumber);
        profile.setUser(user);

        List<BookAuthor> authors = Arrays.stream(favoriteBookAuthors.split(","))
                .map(String::trim)
                .map(authorName -> {
                    Optional<BookAuthor> authorList = bookAuthorMapper.findByName(authorName);
                    if (authorList.isEmpty()) {
                        BookAuthor newAuthor = new BookAuthor(authorName);
                        return bookAuthorMapper.save(newAuthor);
                    } else {
                        return authorList.get();
                    }
                })
                .collect(Collectors.toList());
        profile.setFavouriteBookAuthors(authors);

        List<BookCategory> categories = Arrays.stream(favoriteBookCategories.split(","))
                .map(String::trim)
                .map(categoryName -> {
                    Optional<BookCategory> categoryList = bookCategoryMapper.findByName(categoryName);
                    if (categoryList.isEmpty()) {
                        BookCategory newCategory = new BookCategory(categoryName);
                        return bookCategoryMapper.save(newCategory);
                    } else {
                        return categoryList.get();
                    }
                })
                .collect(Collectors.toList());
        profile.setFavouriteBookCategories(categories);

        userProfileMapper.save(profile);
    }

    public UserProfile getUserProfile(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found for username: " + username));
    }
    @Override
    public List<Book> retrieveBookOffers(String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found for username: " + username));
        return userProfile.getBookOffers();
    }
    @Override
    public void addBookOffer(String username, Book book) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found for username: " + username));
        userProfile.getBookOffers().add(book);
    }

    @Override
    public void requestBook(int bookId, String username) {
        Book book = bookMapper.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + bookId));
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));

        userProfile.getRequestedBooks().add(book);
        book.getRequestingUsers().add(userProfile);

        bookMapper.save(book);

        userProfileRepository.save(userProfile);
    }

    @Override
    public List<Book> retrieveBookRequests(String username) {
        return userProfileRepository.findByUsername(username)
                .map(UserProfile::getRequestedBooks)  // Assuming UserProfile has a getBooks() method.
                .orElse(Collections.emptyList());  // Return an empty list if the user is not found.
    }

    @Override
    public List<UserProfile> retrieveRequestingUsers(int bookId) {
        Book book = bookMapper.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + bookId));

        return book.getRequestingUsers();
    }

    @Override
    public void removeBookOffer(int bookId, String username) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
        Book book = bookMapper.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Invalid book ID"));
        if (userProfile.getBookOffers().contains(book)) {
            List<UserProfile> requestingUsers = userProfileMapper.findAllByRequestedBooksContaining(book);
            for (UserProfile user: requestingUsers) {
                user.getRequestedBooks().remove(book);
                userProfileMapper.save(user);
            }
            userProfile.getBookOffers().remove(book);
            userProfileMapper.save(userProfile);
        }
        else {
            throw new IllegalArgumentException("Error: Book was not found in user " + username + "'s offer list.");
        }
    }

    @Override
    public List<Book> recommendBooks(String username, String recommendationStrategy) {
        UserProfile userProfile = userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found for username: " + username));

        List<BookAuthor> favouriteAuthors = userProfile.getFavouriteBookAuthors();
        List<BookCategory> favouriteCategories = userProfile.getFavouriteBookCategories();

        Set<Book> recommendedBooks = new HashSet<>();
        if (recommendationStrategy == "AuthorsOnly")
            recommendedBooks.addAll(bookMapper.findByBookAuthorsIn(favouriteAuthors));
        else if (recommendationStrategy == "CategoriesOnly")
            recommendedBooks.addAll(bookMapper.findByBookCategoryIn(favouriteCategories));
        else {
            recommendedBooks.addAll(bookMapper.findByBookAuthorsIn(favouriteAuthors));
            recommendedBooks.addAll(bookMapper.findByBookCategoryIn(favouriteCategories));
        }

        return new ArrayList<>(recommendedBooks);
    }
}
