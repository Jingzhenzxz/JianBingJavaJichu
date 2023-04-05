package com.wuan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @TempDir
    Path tempDir;

    UserService userService;

    @BeforeEach
    void setUp() {
        File usersFile = new File(tempDir.toFile(), UserService.DEFAULT_FILE_NAME);
        userService = new UserService(usersFile.getAbsolutePath());
        System.setProperty("user.dir", tempDir.toString());
    }

    @Test
    void testUsernameExists() {
        String username = "testuser";
        assertFalse(userService.usernameExists(username));
        userService.registerUser(username, "P@ssw0rd!");
        assertTrue(userService.usernameExists(username));
    }

    @Test
    void testPasswordIsValidate() {
        assertFalse(userService.passwordIsValidate("12345"));
        assertFalse(userService.passwordIsValidate("password"));
        assertFalse(userService.passwordIsValidate("P@ss"));
        assertTrue(userService.passwordIsValidate("P@ssw0rd!"));
    }

    @Test
    void testRegisterUser() {
        String username = "testuser";
        String password = "P@ssw0rd!";
        assertFalse(userService.usernameExists(username));
        userService.registerUser(username, password);
        assertTrue(userService.usernameExists(username));
    }

    @Test
    void testLoginUser() {
        String username = "testuser";
        String password = "P@ssw0rd!";
        assertFalse(userService.loginUser(username, password));
        userService.registerUser(username, password);
        assertTrue(userService.loginUser(username, password));
    }

    @Test
    void testListUsers() {
        String username1 = "testuser1";
        String username2 = "testuser2";
        String password = "P@ssw0rd!";

        List<String> users = userService.listUsers();
        assertEquals(0, users.size());

        userService.registerUser(username1, password);
        userService.registerUser(username2, password);
        users = userService.listUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(username1));
        assertTrue(users.contains(username2));
    }
}

