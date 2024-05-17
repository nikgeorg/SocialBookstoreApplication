package com.example.SocialBookstoreApplication.domainmodel;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;


public class BookSpecifications {
    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Book> hasAuthorName(String authorName) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, BookAuthor> authors = root.join("bookAuthors");
            return criteriaBuilder.like(authors.get("name"), "%" + authorName + "%");
        };
    }
}