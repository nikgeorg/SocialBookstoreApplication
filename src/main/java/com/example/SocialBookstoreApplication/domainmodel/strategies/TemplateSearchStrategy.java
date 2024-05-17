package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.mappers.BookMapper;
import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.formsdata.BookFormData;
import com.example.SocialBookstoreApplication.formsdata.SearchFormData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class TemplateSearchStrategy implements SearchStrategy {
    @Autowired
    private BookMapper bookMappers;

    @Override
    public List<BookFormData> search(BookFormData bookFormData, BookMapper bookMapper) {

        return List.of();
    }

    public abstract List<Book> makeInitialListOfBooks(SearchFormData searchDto);

    public abstract boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book);
}
