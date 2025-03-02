package com.example.socialonlinebookstore.mappers;

import com.example.socialonlinebookstore.domainmodel.Book;
import com.example.socialonlinebookstore.domainmodel.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookAuthorMapper extends JpaRepository<BookAuthor, Integer> {
    Optional<BookAuthor> findByName(String name);

    BookAuthor findByNameContaining(String name);

}
