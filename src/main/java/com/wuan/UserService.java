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
import java.util.ArrayList;
import java.util.List;

public class UserService {
    static final String DEFAULT_FILE_NAME = "users.xml";
    private final String fileName;

    // 默认构造函数
    public UserService() {
        this.fileName = DEFAULT_FILE_NAME;
    }

    // 新增构造函数，接受文件路径作为参数
    public UserService(String fileName) {
        this.fileName = fileName;
    }

    public boolean usernameExists(String username) {
        boolean usernameExists = false;
        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();

            // Check if the username already exists
            List<Element> userElements = usersElement.elements("user");
            for (Element userElement : userElements) {
                if (username.equals(userElement.element("username").getText())) {
                    usernameExists = true;
                }
            }
        } catch (DocumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return usernameExists;
    }

    public boolean passwordIsValidate(String password) {
        // Password validation using regex
        boolean containsLetter = password.matches(".*[a-zA-Z].*");
        boolean containsDigit = password.matches(".*\\d.*");
        boolean containsSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        boolean hasValidLength = password.length() >= 6;
        return containsLetter && containsDigit && containsSpecial && hasValidLength;
    }

    public void registerUser(String username, String password) {
        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();
            Element userElement = usersElement.addElement("user");
            userElement.addElement("username").setText(username);
            userElement.addElement("password").setText(password);

            saveDocument(document);
            System.out.println("User registered successfully.");
        } catch (DocumentException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public boolean[] loginUser(String username, String password) {
        boolean foundUser = false;
        boolean foundPassword = false;
        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();

            List<Element> userElements = usersElement.elements("user");


            for (Element userElement : userElements) {
                String storedUsername = userElement.element("username").getText();
                String storedPassword = userElement.element("password").getText();

                if (username.equals(storedUsername)) {
                    foundUser = true;
                    if (password.equals(storedPassword)) {
                        foundPassword = true;
                    }
                    break;
                }

            }
        } catch (DocumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new boolean[]{foundUser, foundPassword};
    }

    public List<String> listUsers() {
        List<String> userList = new ArrayList<>();
        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();
            List<Element> userElements = usersElement.elements("user");
            int userCount = userElements.size();

            if (userCount != 0) {
                for (Element userElement : userElements) {
                    String username = userElement.element("username").getText();
                    userList.add(username);
                }
            }
        } catch (DocumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return userList;
    }

    private Document getOrCreateDocument() throws DocumentException {
        File file = new File(fileName);
        if (file.exists()) {
            SAXReader reader = new SAXReader();
            return reader.read(file);
        } else {
            Document document = DocumentHelper.createDocument();
            document.addElement("users");
            return document;
        }
    }

    private void saveDocument(Document document) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileWriter(fileName), format);
        writer.write(document);
        writer.close();
    }
}