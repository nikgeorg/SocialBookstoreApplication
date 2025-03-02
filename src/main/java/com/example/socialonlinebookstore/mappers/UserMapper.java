package com.example.socialonlinebookstore.mappers;

import com.example.socialonlinebookstore.domainmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMapper extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findUserByUsername(String username);
}
