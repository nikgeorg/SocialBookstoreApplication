package com.example.socialonlinebookstore.mappers;

import com.example.socialonlinebookstore.domainmodel.Book;
import com.example.socialonlinebookstore.domainmodel.User;
import com.example.socialonlinebookstore.domainmodel.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileMapper extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByUser(User user);

    Optional<UserProfile> findByUsername(String username);

    List<UserProfile> findAllByRequestedBooksContaining(Book book);
}
