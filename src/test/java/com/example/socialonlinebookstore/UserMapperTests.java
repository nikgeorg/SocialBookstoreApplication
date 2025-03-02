package com.example.socialonlinebookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.socialonlinebookstore.domainmodel.User;
import com.example.socialonlinebookstore.mappers.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class UserMapperTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testFindByUsername() {
        String username = "petros09";
        User user = userMapper.findByUsername(username).orElse(null);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }
}