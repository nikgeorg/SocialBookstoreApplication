package com.example.SocialBookstoreApplication.datamappers;

import com.example.SocialBookstoreApplication.domainmodel.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserMapper extends JpaRepository<User, Integer> {
    User findByUsername(String username);

}
