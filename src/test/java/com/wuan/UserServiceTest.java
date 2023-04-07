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
    // 在 C:\Users\“你的用户名”\AppData\Local\Temp 中随机生成一个临时目录，测试结束后会自动删除
    Path tempDir;

    UserService userService;

    @BeforeEach
    // 每个测试用例执行前都会执行一次
    void setUp() {
         /*
         在临时目录下创建一个名 userFile 的对象，
         这个对象表示位于临时目录中的 "UserService.DEFAULT_FILE_NAME" 文件。如果文件不存在，
         这个 File 对象可以用于创建新文件；如果文件已经存在，这个 File 对象可以用于访问和操作该文件。
         userFile 的路径由两部分组成：一个目录和一个文件名。
         目录由 tempDir.toFile() 指定，Path.toFile() 方法可以将 Path 对象转换为 File 对象，
         两者在底层都表示文件系统中的文件和目录。
         文件名由 UserService.DEFAULT_FILE_NAME 指定。
         */
        File usersFile = new File(tempDir.toFile(), UserService.DEFAULT_FILE_NAME);
        // 创建 UserService 对象，传入文件路径
        userService = new UserService(usersFile.getAbsolutePath());
        /*
         设置系统属性 "user.dir" 的值为临时目录的路径。这将使得在测试期间，
         程序将临时目录视为当前工作目录，以避免在实际工作目录中创建或修改文件。
         注意， "user.dir" 是 Java 的一个特定系统属性，而非操作系统的环境变量。要获取 "user.dir" 系统属性，可以使用以下代码：
         String userDir = System.getProperty("user.dir");
         */
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
    void testLoginUsername() {
        String username = "testuser";
        String password = "P@ssw0rd!";
        assertFalse(userService.loginUser(username, password)[0]);
        userService.registerUser(username, password);
        assertTrue(userService.loginUser(username, password)[0]);
    }

    @Test
    void testLoginUserPassword() {
        String username = "testuser";
        String password = "P@ssw0rd!";
        String wrongPassword = "P@ssw0rd.";
        userService.registerUser(username, password);
        assertFalse(userService.loginUser(username, wrongPassword)[1]);
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

