package com.wuan;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class UserSystemTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testRegisterUser() {
        String testUsername = "testuser";
        String testPassword = "Test@123";

        UserSystem.registerUser(testUsername, testPassword);

        String expectedOutput = "User registered successfully.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testRegisterUserWithExistingUsername() {
        String testUsername = "testuser";
        String testPassword = "Test@123";

        UserSystem.registerUser(testUsername, testPassword);
        outContent.reset();

        UserSystem.registerUser(testUsername, testPassword);
        String expectedOutput = "Username already exists. Please try again.\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    // 更多测试方法...
}
