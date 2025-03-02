package com.example.socialonlinebookstore.services;

import org.springframework.stereotype.Service;

import com.example.socialonlinebookstore.domainmodel.User;

@Service
public interface UserService {
    void saveUser(User user);

    boolean isUserPresent(User user);

    User findById(String username);

    boolean hasProfile(String username);
}