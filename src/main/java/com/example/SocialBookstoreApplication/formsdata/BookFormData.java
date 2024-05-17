package com.example.SocialBookstoreApplication.formsdata;

import java.util.List;

public class BookFormData {
        private int bookId;
        private String title;
        private List<String> authorNames;
        private String categoryName;


    // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public List<String> getAuthorNames() {
            return authorNames;
        }

        public void setAuthorName(List<String> authorNames) {
            this.authorNames = authorNames;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
