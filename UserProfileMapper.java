package com.example.SocialBookstoreApplication.datamappers;
import com.example.SocialBookstoreApplication.domainmodel.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileMapper extends JpaRepository<UserProfile, Integer> {
    UserProfile findByUsername(String username);
}
