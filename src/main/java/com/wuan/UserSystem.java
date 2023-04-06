package com.wuan;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class UserSystem {
    private static final UserService userService = new UserService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
        boolean running = true;

        while (running) {
            System.out.println("Please choose an action: 1: Register, 2: Login, 3: List users, 4: Quit");
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1":
                    System.out.println("Please enter your username:");
                    String username = scanner.nextLine().trim();
                    while (userService.usernameExists(username)) {
                        System.out.println("Username already exists. Please try again.");
                        username = scanner.nextLine().trim();
                    }

                    System.out.println("Please enter your password:");
                    System.out.println("(Password must contain at least one letter, one digit," +
                            " and one special character and be at least 6 characters long.)");
                    String password = scanner.nextLine().trim();
                    while (!userService.passwordIsValidate(password)) {
                        System.out.println("User registered failed." +
                                " Password must contain at least one letter, one digit, and" +
                                " one special character" +
                                " and be at least 6 characters long." +
                                " Please enter your password again.");
                        password = scanner.nextLine().trim();
                    }

                    userService.registerUser(username, password);
                    break;
                case "2":
                    System.out.println("Please enter your username:");
                    String usernameLogin = scanner.nextLine().trim();
                    System.out.println("Please enter your password:");
                    String passwordLogin = scanner.nextLine().trim();

                    boolean[] loginResult = userService.loginUser(usernameLogin, passwordLogin);
                    boolean foundUser = loginResult[0];
                    boolean foundPassword = loginResult[1];
                    if (foundUser) {
                        if (foundPassword) {
                            System.out.println("Logged in successfully.");
                        } else {
                            System.out.println("Invalid password.");
                        }
                    } else {
                        System.out.println("Username doesn't exist.");
                    }
                    break;
                case "3":
                    List<String> userList = userService.listUsers();
                    if (userList.isEmpty()) {
                        System.out.println("No users found.");
                    } else {
                        System.out.println("User count: " + userList.size());
                        System.out.println("User list:");
                        for (String user : userList) {
                            System.out.println(" - " + user);
                        }
                    }
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid action. Please try again.");
            }
        }

        scanner.close();
    }
}
