package com.wuan;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class UserSystem {

    private static final String FILE_NAME = "users.xml";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Please choose an action: 1: Register, 2: Login, 3: List users, 4: Quit");
            String action = scanner.nextLine().trim();

            switch (action) {
                case "1":
                    registerUser(scanner);
                    break;
                case "2":
                    loginUser(scanner);
                    break;
                case "3":
                    listUsers();
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

    public static void registerUser(Scanner scanner) {
        System.out.println("Please enter your username:");
        String username = scanner.nextLine().trim();

        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();

            // Check if the username already exists
            List<Element> userElements = usersElement.elements("user");
            boolean usernameExists = false;
            for (Element userElement : userElements) {
                if (username.equals(userElement.element("username").getText())) {
                    usernameExists = true;
                    break;
                }
            }

            if (usernameExists) {
                System.out.println("Username already exists. Please try again.");
            } else {
                System.out.println("Please enter your password:");
                System.out.println("(Password must contain at least one letter, one digit," +
                        " and one special character and be at least 6 characters long.)");
                String password = scanner.nextLine().trim();

                // Password validation using regex
                boolean containsLetter = password.matches(".*[a-zA-Z].*");
                boolean containsDigit = password.matches(".*\\d.*");
                boolean containsSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
                boolean hasValidLength = password.length() >= 6;

                if (containsLetter && containsDigit && containsSpecial && hasValidLength) {
                    Element userElement = usersElement.addElement("user");
                    userElement.addElement("username").setText(username);
                    userElement.addElement("password").setText(password);

                    saveDocument(document);
                    System.out.println("User registered successfully.");
                } else {
                    System.out.println("User registered failed." +
                            " Password must contain at least one letter, one digit, and one special character" +
                            " and be at least 6 characters long.");
                }
            }
        } catch (DocumentException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public static void loginUser(Scanner scanner) {
        System.out.println("Please enter your username:");
        String username = scanner.nextLine().trim();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine().trim();

        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();

            List<Element> userElements = usersElement.elements("user");

            boolean found = false;
            for (Element userElement : userElements) {
                String storedUsername = userElement.element("username").getText();
                String storedPassword = userElement.element("password").getText();

                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                System.out.println("Logged in successfully.");
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (DocumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void listUsers() {
        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();
            List<Element> userElements = usersElement.elements("user");
            int userCount = userElements.size();

            if (userCount == 0) {
                System.out.println("No users found.");
            } else {
                System.out.println("User count: " + userCount);
                System.out.println("User list:");
                for (Element userElement : userElements) {
                    String username = userElement.element("username").getText();
                    System.out.println(" - " + username);
                }
            }
        } catch (DocumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Document getOrCreateDocument() throws DocumentException {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            SAXReader reader = new SAXReader();
            return reader.read(file);
        } else {
            Document document = DocumentHelper.createDocument();
            document.addElement("users");
            return document;
        }
    }

    private static void saveDocument(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(FILE_NAME), format);
        writer.write(document);
        writer.close();
    }
}