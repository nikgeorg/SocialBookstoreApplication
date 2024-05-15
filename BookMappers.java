package com.example.SocialBookstoreApplication.datamappers;

import com.example.SocialBookstoreApplication.domainmodel.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookMappers extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);
}
