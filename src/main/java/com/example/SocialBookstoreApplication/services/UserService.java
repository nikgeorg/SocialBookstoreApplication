package com.example.SocialBookstoreApplication.services;

import org.springframework.stereotype.Service;

import com.example.SocialBookstoreApplication.domainmodel.User;

@Service
public interface UserService {
    public void saveUser(User user);
    public boolean isUserPresent(User user);
    public User findById(String username);
}