package com.example.SocialBookstoreApplication.mappers;

import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.domainmodel.BookAuthor;
import com.example.SocialBookstoreApplication.domainmodel.BookCategory;
import com.example.SocialBookstoreApplication.formsdata.BookFormData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper extends JpaRepository<Book, Integer> {
    List<Book> findByTitle(String title);

    @Mapping(source = "authorNames", target = "bookAuthors")
    @Mapping(source = "categoryName", target = "bookCategory.name")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "bookId", target = "bookId")
    Book toBook(BookFormData bookFormData);
    @Mapping(source = "bookAuthors", target = "authorNames")
    @Mapping(source = "bookCategory.name", target = "categoryName")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "bookId", target = "bookId")
    BookFormData toBookFormData(Book book);

    Book findByBookId(int bookId);
}
