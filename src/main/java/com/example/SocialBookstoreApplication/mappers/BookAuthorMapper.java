package com.example.SocialBookstoreApplication.mappers;

import com.example.SocialBookstoreApplication.domainmodel.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookAuthorMapper extends JpaRepository<BookAuthor, Integer> {
   List<BookAuthor> findByName(String name);
}
