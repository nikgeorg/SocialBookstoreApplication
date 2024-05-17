package com.example.SocialBookstoreApplication.domainmodel;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class UserProfile {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<BookAuthor> getFavouriteBookAuthors() {
        return favouriteBookAuthors;
    }

    public void setFavouriteBookAuthors(List<BookAuthor> favouriteBookAuthors) {
        this.favouriteBookAuthors = favouriteBookAuthors;
    }

    public List<BookCategory> getFavouriteBookCategories() {
        return favouriteBookCategories;
    }

    public void setFavouriteBookCategories(List<BookCategory> favouriteBookCategories) {
        this.favouriteBookCategories = favouriteBookCategories;
    }

    public List<Book> getBookOffers() {
        return bookOffers;
    }

    public void setBookOffers(List<Book> bookOffers) {
        this.bookOffers = bookOffers;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String username;
    private String fullName;
    private int age;
    @ManyToMany
    private List<BookAuthor> favouriteBookAuthors;
    @ManyToMany
    private List<BookCategory> favouriteBookCategories;
    @ManyToMany
    private List<Book> bookOffers;
    @ManyToMany(mappedBy = "requestingUsers")
    private List<Book> requestedBooks;

    public List<Book> getRequestedBooks() {
        return requestedBooks;
    }

    public void setRequestedBooks(List<Book> requestedBooks) {
        this.requestedBooks = requestedBooks;
    }
}
