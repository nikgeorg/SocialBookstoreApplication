package com.example.SocialBookstoreApplication.formsdata;

import com.example.SocialBookstoreApplication.domainmodel.BookCategory;

import java.util.List;

public class RecommendationsFormData {
        private String category;
        private List<BookCategory> preferredCategories;
        private String author;
        private String strategyType;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<BookCategory> getPreferredCategories() {
            return preferredCategories;
        }

        public void setPreferredCategories(List<BookCategory> preferredCategories) {
            this.preferredCategories = preferredCategories;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getStrategyType() {
            return strategyType;
        }

        public void setStrategyType(String strategyType) {
            this.strategyType = strategyType;
        }
}
