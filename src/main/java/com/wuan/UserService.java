package com.wuan;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    static final String DEFAULT_FILE_NAME = "users.xml";
    private final String fileName;

    // 默认构造函数
    public UserService() {
        this.fileName = DEFAULT_FILE_NAME;
    }

    // 新增构造函数，接受文件路径作为参数，测试的时候会用到
    public UserService(String fileName) {
        this.fileName = fileName;
    }

    public boolean usernameExists(String username) {
        boolean usernameExists = false;
        try {
            // 获取或创建 XML 文档
            Document document = getOrCreateDocument();
            // 获取根节点
            Element usersElement = document.getRootElement();

            // 获取所有 user 节点，存到一个 List 中
            List<Element> userElements = usersElement.elements("user");
            // 遍历 List，判断 username 是否存在于 XML 文档中
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
        // 利用正则表达式判断密码是否包含字母、数字、特殊字符，且长度大于等于 6
        boolean containsLetter = password.matches(".*[a-zA-Z].*");
        boolean containsDigit = password.matches(".*\\d.*");
        boolean containsSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        boolean hasValidLength = password.length() >= 6;
        return containsLetter && containsDigit && containsSpecial && hasValidLength;
    }

    public void registerUser(String username, String password) {
        try {
            // 获取或创建 XML 文档
            Document document = getOrCreateDocument();
            // 获取根节点
            Element usersElement = document.getRootElement();
            // 创建 user 节点
            Element userElement = usersElement.addElement("user");
            // 创建 username 节点，并设置文本内容为 username 所传入的值
            userElement.addElement("username").setText(username);
            // 创建 password 节点，并设置文本内容为 password 所传入的值
            userElement.addElement("password").setText(password);

            // 保存 XML 文档
            saveDocument(document);
            System.out.println("User registered successfully.");
        } catch (DocumentException | IOException e) {
            // DocumentException 发生于 getOrCreateDocument() 方法中，IOException 发生于 saveDocument() 方法中
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

            // 遍历 List，判断 username 是否存在于 XML 文档中
            for (Element userElement : userElements) {
                // 获取 username 节点的文本内容
                String storedUsername = userElement.element("username").getText();
                // 获取 password 节点的文本内容
                String storedPassword = userElement.element("password").getText();

                // 每次循环都会判断 username 是否存在于 XML 文档中，如果存在，就说明找到了该用户，
                // 于是将 foundUser 设置为 true
                if (username.equals(storedUsername)) {
                    foundUser = true;
                    // 如果找到了该用户，就判断 password 是否正确，如果正确，就说明输入的密码也正确，
                    // 于是将 foundPassword 设置为 true
                    if (password.equals(storedPassword)) {
                        foundPassword = true;
                    }
                    // 无论 password 是否正确，都要跳出循环，因为已经找到了该用户
                    break;
                }

            }
        } catch (DocumentException e) {
            // DocumentException 发生于 getOrCreateDocument() 方法中
            System.out.println("Error: " + e.getMessage());
        }
        // 返回一个 boolean 数组，第一个元素表示是否找到了该用户，第二个元素表示该用户的密码是否正确
        return new boolean[]{foundUser, foundPassword};
    }

    public List<String> listUsers() {
        // 创建一个 List，用于存储所有的 username
        List<String> userList = new ArrayList<>();
        try {
            Document document = getOrCreateDocument();
            Element usersElement = document.getRootElement();

            List<Element> userElements = usersElement.elements("user");
            int userCount = userElements.size();

            // 如果 userCount 不为 0，说明 XML 文档中至少有一个 user 节点，
            // 于是遍历 List，将每个 user 节点的 username 节点的文本内容添加到 userList 中
            if (userCount != 0) {
                for (Element userElement : userElements) {
                    // 获取 username 节点的文本内容
                    String username = userElement.element("username").getText();
                    // 将 username 添加到 userList 中
                    userList.add(username);
                }
            }
        } catch (DocumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // 返回 userList
        return userList;
    }

    private Document getOrCreateDocument() throws DocumentException { // 此处抛出了 DocumentException 异常，
        // 可以在调用方法中用 try-catch 捕获。

        // 创建一个 File 对象 file，其路径为 fileName 所指定的路径
        File file = new File(fileName);
        // 如果 file 对象所指定的文件存在，就读取该文件，否则就创建一个新的 XML 文档
        if (file.exists()) {
            // 创建一个 SAXReader 对象，用于读取 XML 文件
            SAXReader reader = new SAXReader();
            // read(File file) 方法会读取 file 所指定的 XML 文档，并返回一个 Document 对象
            return reader.read(file);
        } else {
            // createDocument() 方法会创建一个新的 XML 文档，并返回一个 Document 对象
            Document document = DocumentHelper.createDocument();
            // 创建一个根节点 users
            document.addElement("users");
            return document;
        }
    }

    private void saveDocument(Document document) throws IOException { // 此处抛出了 IOException 异常，
        // 可以在调用方法中用 try-catch 捕获

        // OutputFormat.createPrettyPrint() 方法会生成一个格式化的 XML 文档，默认是 UTF-8 编码
        OutputFormat format = OutputFormat.createPrettyPrint();

        /*
         * Paths.get(fileName) 方法会返回一个 Path 对象，该对象表示 fileName 所指定的文件路径。
         * Files.newBufferedWriter(Paths.get(fileName)) 方法会创建一个 UTF-8 编码的
         * BufferedWriter 对象，该 BufferedWriter 对象会写入 fileName 所指定的文件。
         * new XMLWriter(Files.newBufferedWriter(Paths.get(fileName)), format) 方法
         * 会创建一个 XMLWriter 对象，其格式为 format 所指定的格式。
         */
        XMLWriter writer = new XMLWriter(Files.newBufferedWriter(Paths.get(fileName)), format);
        // 将 Document 对象写入 fileName 所指定的文件
        writer.write(document);
        // 关闭 XMLWriter 对象，释放与 XMLWriter 对象关联的资源
        writer.close();
    }
}