package com.example.SocialBookstoreApplication.datamappers;

import com.example.SocialBookstoreApplication.domainmodel.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookAuthorMapper extends JpaRepository<BookAuthor, Integer> {
    List<BookAuthor> findByName(String name);
}
