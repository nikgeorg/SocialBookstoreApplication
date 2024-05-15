package com.example.SocialBookstoreApplication.datamappers;

import com.example.SocialBookstoreApplication.domainmodel.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryMapper extends JpaRepository<BookCategory, Integer> {
    List<BookCategory> findByName(String name);

}
