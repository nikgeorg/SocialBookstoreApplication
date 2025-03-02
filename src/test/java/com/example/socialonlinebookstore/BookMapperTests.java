package com.example.socialonlinebookstore;

import com.example.socialonlinebookstore.domainmodel.Book;
import com.example.socialonlinebookstore.domainmodel.BookAuthor;
import com.example.socialonlinebookstore.domainmodel.BookCategory;
import com.example.socialonlinebookstore.mappers.BookAuthorMapper;
import com.example.socialonlinebookstore.mappers.BookCategoryMapper;
import com.example.socialonlinebookstore.mappers.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class BookMapperTests {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookAuthorMapper bookAuthorMapper;

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    @Test
    @Transactional
    void testFindByTitle() {
        // create and save author
        BookAuthor harperLee = new BookAuthor("Harper Lee");
        bookAuthorMapper.save(harperLee);

        // create and save category
        BookCategory historicalFiction = new BookCategory("Historical Fiction");
        bookCategoryMapper.save(historicalFiction);

        // create and save book
        Book newBook = new Book("To Kill a Mockingbird", Collections.singletonList(harperLee), historicalFiction, "A summary");
        bookMapper.save(newBook);

        // find book based on title
        String title = "To Kill a Mockingbird";
        Book book = bookMapper.findByTitle(title).orElse(null);

        // verify book was found and title is also correct
        assertNotNull(book);
        assertEquals(title, book.getTitle());
        assertEquals("Harper Lee", book.getBookAuthors().get(0).getName());
        assertEquals("Historical Fiction", book.getBookCategory().getName());
    }
}