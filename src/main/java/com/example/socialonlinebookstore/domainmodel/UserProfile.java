package com.example.socialonlinebookstore.domainmodel;

import aj.org.objectweb.asm.commons.Remapper;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users_profile")
public class UserProfile {

    @Id
    @Column(name = "user_name")
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "user_favourite_authors",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private List<BookAuthor> favouriteBookAuthors;

    @ManyToMany
    @JoinTable(
            name = "user_favourite_categories",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<BookCategory> favouriteBookCategories;

    @ManyToMany
    @JoinTable(
            name = "user_book_offers",
            joinColumns = @JoinColumn(name = "user_name"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> bookOffers;

    @ManyToMany(mappedBy = "requestingUsers")
    private List<Book> requestedBooks = new ArrayList<>();

    // Constructors, getters, and setters
    public UserProfile() {}

    public UserProfile(String username, String fullName, int age, int phoneNumber) {
        this.username = username;
        this.fullName = fullName;
        this.age = age;
    }

    public UserProfile(String username, String fullName, int age, User user) {
        this.username = username;
        this.fullName = fullName;
        this.age = age;
        this.user = user;
    }

    public UserProfile(String username, String fullName, int age, String address, String phoneNumber) {
        this.username = username;
        this.fullName = fullName;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<Book> getRequestedBooks() {
        return requestedBooks;
    }

    public void setRequestedBooks(List<Book> requestedBooks) {
        this.requestedBooks = requestedBooks;
    }

    @Override
    public String toString() {
        return username;
    }

}