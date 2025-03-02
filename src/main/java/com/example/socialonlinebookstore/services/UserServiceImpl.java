package com.example.socialonlinebookstore.services;

import com.example.socialonlinebookstore.domainmodel.User;
import com.example.socialonlinebookstore.mappers.UserMapper;
import com.example.socialonlinebookstore.mappers.UserProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userMapper.save(user);
    }

    @Override
    public boolean isUserPresent(User user) {
        Optional<User> storedUser = userMapper.findByUsername(user.getUsername());
        return storedUser.isPresent();
    }

    @Override
    public User findById(String username) {
        return userMapper.findUserByUsername(username);
    }

    // Method defined in Spring Security UserDetailsService interface
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // orElseThrow method of Optional container that throws an exception if Optional result  is null
        return userMapper.findByUsername(username).orElseThrow(
                ()-> new UsernameNotFoundException(
                        String.format("USER_NOT_FOUND %s", username)
                ));
    }
    @Override
    public boolean hasProfile(String username) {
        return userMapper.findByUsername(username).isPresent();
    }
}