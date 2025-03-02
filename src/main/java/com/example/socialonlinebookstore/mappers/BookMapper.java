package com.example.socialonlinebookstore.mappers;

import com.example.socialonlinebookstore.domainmodel.Book;
import com.example.socialonlinebookstore.domainmodel.BookAuthor;
import com.example.socialonlinebookstore.domainmodel.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookMapper extends JpaRepository<Book, Integer> {
    Optional<Book> findByTitle(String title);

    List<Book> findByTitleContaining(String title);
    List<Book> findByTitleAndBookAuthorsIn(String title, List<BookAuthor> authors);
    List<Book> findByTitleContainingAndBookAuthorsIn(String title, List<BookAuthor> authors);

    List<Book> findByTitleAndBookAuthorsInAndBookCategory(String title, List<BookAuthor> authorNames, BookCategory bookCategory);

    List<Book> findByBookAuthorsIn(List<BookAuthor> authorNames);

    Collection<? extends Book> findByBookCategoryIn(List<BookCategory> favouriteCategories);
}
