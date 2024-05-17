package com.example.SocialBookstoreApplication.mappers;

import com.example.SocialBookstoreApplication.domainmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMapper extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    User findUserByUsername(String username);
}
