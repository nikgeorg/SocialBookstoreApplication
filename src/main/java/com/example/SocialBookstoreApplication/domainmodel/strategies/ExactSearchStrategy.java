package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.mappers.BookMapper;
import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.domainmodel.BookAuthor;
import com.example.SocialBookstoreApplication.formsdata.SearchFormData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ExactSearchStrategy extends TemplateSearchStrategy{
    @Autowired
    private BookMapper bookMapper;

    public List<Book> makeInitialListOfBooks(SearchFormData searchDto) {
        return bookMapper.findAll();
    }

    public boolean checkIfAuthorsMatch(SearchFormData searchFormData, Book book) {
        String search = searchFormData.getSearch();
        for (BookAuthor author : book.getBookAuthors()) {
            if (author.getName().equalsIgnoreCase(search)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Book> search(SearchFormData searchFormData) {
        List<Book> initialList = makeInitialListOfBooks(searchFormData);
        return initialList.stream()
                .filter(book -> checkIfAuthorsMatch(searchFormData, book))
                .collect(Collectors.toList());
    }
}
