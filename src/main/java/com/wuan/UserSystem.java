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
                    // trim() 方法用于删除字符串的头尾空白符
                    String username = scanner.nextLine().trim();
                    // 判断用户名是否已经存在
                    while (userService.usernameExists(username)) {
                        // 如果用户名已经存在，提示用户重新输入
                        System.out.println("Username already exists. Please try again.");
                        // 读取用户输入的用户名
                        username = scanner.nextLine().trim();
                    }

                    System.out.println("Please enter your password:");
                    System.out.println("(Password must contain at least one letter, one digit," +
                            " and one special character and be at least 6 characters long.)");
                    // 读取用户输入的密码
                    String password = scanner.nextLine().trim();
                    // 判断密码是否符合要求
                    while (!userService.passwordIsValidate(password)) {
                        // 如果密码不符合要求，提示用户重新输入
                        System.out.println("User registered failed." +
                                " Password must contain at least one letter, one digit, and" +
                                " one special character" +
                                " and be at least 6 characters long." +
                                " Please enter your password again.");
                        // 读取用户输入的密码
                        password = scanner.nextLine().trim();
                    }

                    // 调用 UserService 的 registerUser() 方法注册用户
                    userService.registerUser(username, password);
                    break;
                case "2":
                    System.out.println("Please enter your username:");
                    String usernameLogin = scanner.nextLine().trim();
                    System.out.println("Please enter your password:");
                    String passwordLogin = scanner.nextLine().trim();

                    // 调用 UserService 的 loginUser() 方法登录用户
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
                    // 调用 UserService 的 listUsers() 方法获取用户列表
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
