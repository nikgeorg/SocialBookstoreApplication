package com.example.SocialBookstoreApplication.domainmodel.strategies;

import com.example.SocialBookstoreApplication.mappers.BookMapper;
import com.example.SocialBookstoreApplication.domainmodel.Book;
import com.example.SocialBookstoreApplication.formsdata.BookFormData;
import com.example.SocialBookstoreApplication.formsdata.SearchFormData;


import java.util.List;

public interface SearchStrategy {
    public List<BookFormData> search(BookFormData bookFormData, BookMapper bookMapper);

    public List<Book> search(SearchFormData searchFormData);
}
