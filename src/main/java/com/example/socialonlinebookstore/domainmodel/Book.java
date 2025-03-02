package com.example.socialonlinebookstore.domainmodel;
import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "user_name")
    private String userName;

    @ManyToMany
    @JoinTable(
            name = "book_author_books",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<BookAuthor> bookAuthors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryΙd", nullable = false)
    private BookCategory bookCategory;

    @ManyToMany
    @JoinTable(
            name = "book_requests",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_name")
    )
    private List<UserProfile> requestingUsers;

    // Constructors, getters, and setters
    public Book() {}

    public Book(String title, List<BookAuthor> bookAuthors, BookCategory bookCategory, String summary) {
        this.title = title;
        this.bookAuthors = bookAuthors;
        this.bookCategory = bookCategory;
        this.summary = summary;
    }

    public long getBookId() {
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return userName;
    }


}