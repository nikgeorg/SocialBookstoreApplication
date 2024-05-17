CREATE DATABASE IF NOT EXISTS socialbookstore_database;

USE socialbookstore_database;

-- Create users table
DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       user_id INT NOT NULL AUTO_INCREMENT,
                       user_name VARCHAR(255) UNIQUE DEFAULT NULL,
                       password VARCHAR(255) DEFAULT NULL,
                       role VARCHAR(63) DEFAULT NULL,
                       PRIMARY KEY (user_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- Create user_profile table
DROP TABLE IF EXISTS user_profile;
CREATE TABLE user_profile (
                              user_name VARCHAR(255) NOT NULL,
                              full_name VARCHAR(511) DEFAULT NULL,
                              age TINYINT DEFAULT NULL,
                              PRIMARY KEY (user_name),
                              FOREIGN KEY (user_name) REFERENCES users(user_name) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create book_categories table
DROP TABLE IF EXISTS book_categories;
CREATE TABLE book_categories (
                                 category_id INT NOT NULL AUTO_INCREMENT,
                                 category_name VARCHAR(255) NOT NULL,
                                 PRIMARY KEY (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create books table
DROP TABLE IF EXISTS books;
CREATE TABLE books (
                       book_id INT NOT NULL AUTO_INCREMENT,
                       book_title VARCHAR(511) NOT NULL,
                       book_category_id INT NOT NULL,
                       PRIMARY KEY (book_id),
                       FOREIGN KEY (book_category_id) REFERENCES book_categories(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create book_offers table
DROP TABLE IF EXISTS book_offers;
CREATE TABLE book_offers (
                             book_id INT NOT NULL,
                             book_giver VARCHAR(511) NOT NULL,
                             PRIMARY KEY (book_id),
                             FOREIGN KEY (book_id) REFERENCES books(book_id),
                             FOREIGN KEY (book_giver) REFERENCES user_profile(user_name) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create requested_books table
DROP TABLE IF EXISTS requested_books;
CREATE TABLE requested_books (
                                 book_id INT NOT NULL,
                                 requesting_user VARCHAR(255) NOT NULL,
                                 PRIMARY KEY (book_id, requesting_user),
                                 FOREIGN KEY (book_id) REFERENCES books(book_id),
                                 FOREIGN KEY (requesting_user) REFERENCES user_profile(user_name) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create authors table
DROP TABLE IF EXISTS authors;
CREATE TABLE authors (
                         author_id INT NOT NULL AUTO_INCREMENT,
                         author_name VARCHAR(255) DEFAULT NULL,
                         PRIMARY KEY (author_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create book_authors table for many-to-many relationship between books and authors
DROP TABLE IF EXISTS book_authors;
CREATE TABLE book_authors (
                              author_id INT NOT NULL,
                              book_id INT NOT NULL,
                              PRIMARY KEY (author_id, book_id),
                              FOREIGN KEY (author_id) REFERENCES authors(author_id),
                              FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create liked_authors table for many-to-many relationship between user_profile and authors
DROP TABLE IF EXISTS liked_authors;
CREATE TABLE liked_authors (
                               user_name VARCHAR(255) NOT NULL,
                               author_id INT NOT NULL,
                               PRIMARY KEY (user_name, author_id),
                               FOREIGN KEY (user_name) REFERENCES user_profile(user_name) ON DELETE CASCADE,
                               FOREIGN KEY (author_id) REFERENCES authors(author_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Create liked_categories table for many-to-many relationship between user_profile and book categories
DROP TABLE IF EXISTS liked_categories;
CREATE TABLE liked_categories (
                                  user_name VARCHAR(255) NOT NULL,
                                  category_id INT NOT NULL,
                                  PRIMARY KEY (user_name, category_id),
                                  FOREIGN KEY (user_name) REFERENCES user_profile(user_name) ON DELETE CASCADE,
                                  FOREIGN KEY (category_id) REFERENCES book_categories(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- Insert initial data into users table
INSERT INTO users (user_name, password, role) VALUES
                                                  ('john_doe', 'password123', 'user'),
                                                  ('jane_smith', 'securepass', 'user'),
                                                  ('admin_user', 'adminpass', 'admin');

-- Insert initial data into user_profile table
INSERT INTO user_profile (user_name, full_name, age) VALUES
                                                         ('john_doe', 'John Doe', 30),
                                                         ('jane_smith', 'Jane Smith', 25),
                                                         ('admin_user', 'Admin User', 35);

-- Insert initial data into book_categories table
INSERT INTO book_categories (category_name) VALUES
                                                ('Fiction'),
                                                ('Dystopian'),
                                                ('Science Fiction'),
                                                ('Fantasy'),
                                                ('Biography');

-- Insert initial data into books table
INSERT INTO books (book_title, book_category_id) VALUES
                                                     ('The Great Gatsby', 1),
                                                     ('To Kill a Mockingbird', 1),
                                                     ('1984', 2),
                                                     ('Moby Dick', 1),
                                                     ('The Catcher in the Rye', 1);

-- Insert initial data into book_offers table
INSERT INTO book_offers (book_id, book_giver) VALUES
                                                  (1, 'john_doe'),
                                                  (2, 'john_doe'),
                                                  (3, 'jane_smith');

-- Insert initial data into requested_books table
INSERT INTO requested_books (book_id, requesting_user) VALUES
                                                           (4, 'jane_smith'),
                                                           (5, 'john_doe');

-- Insert initial data into authors table
INSERT INTO authors (author_name) VALUES
                                      ('F. Scott Fitzgerald'),
                                      ('Harper Lee'),
                                      ('George Orwell'),
                                      ('Herman Melville'),
                                      ('J.D. Salinger');

-- Insert initial data into book_authors table
INSERT INTO book_authors (author_id, book_id) VALUES
                                                  (1, 1),
                                                  (2, 2),
                                                  (3, 3),
                                                  (4, 4),
                                                  (5, 5);

-- Insert initial data into liked_authors table
INSERT INTO liked_authors (user_name, author_id) VALUES
                                                     ('john_doe', 1),
                                                     ('jane_smith', 2),
                                                     ('admin_user', 3);

-- Insert initial data into liked_categories table
INSERT INTO liked_categories (user_name, category_id) VALUES
                                                          ('john_doe', 1),
                                                          ('jane_smith', 2),
                                                          ('admin_user', 1);