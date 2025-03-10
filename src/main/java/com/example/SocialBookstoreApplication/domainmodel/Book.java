package com.example.SocialBookstoreApplication.domainmodel;
import jakarta.persistence.*;

import java.util.List;
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    private String title;
    @ManyToMany
    private List<BookAuthor> bookAuthors;
    @ManyToOne
    private BookCategory bookCategory;
    @ManyToMany(mappedBy = "requestedBooks")
    private List<UserProfile> requestingUsers;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }

    public List<UserProfile> getRequestingUsers() {
        return requestingUsers;
    }

    public void setRequestingUsers(List<UserProfile> requestingUsers) {
        this.requestingUsers = requestingUsers;
    }
}
