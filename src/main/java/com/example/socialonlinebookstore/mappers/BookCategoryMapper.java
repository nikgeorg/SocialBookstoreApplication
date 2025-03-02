package com.example.socialonlinebookstore.mappers;

import com.example.socialonlinebookstore.domainmodel.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCategoryMapper extends JpaRepository<BookCategory, Integer> {
    Optional<BookCategory> findByName(String name);
}