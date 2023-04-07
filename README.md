# 简易命令行登录注册系统

一个使用 Java 和 dom4j 实现的简易命令行登录注册系统。

## 功能

- 注册用户
- 用户登录
- 查询用户数量和信息
- 退出程序

## 用法

1. 克隆本仓库到本地。
    
    ```bash
    $ git clone https://github.com/Jingzhenzxz/JianBingJavaJichu.git
    ```

2. 使用 Maven 编译和运行项目，或使用您喜欢的 IDE 导入项目并运行 UserSystem 类。
        
   ```bash
   $ cd JianBingJavaJichu
   $ mvn compile exec:java -D"exec.mainClass"="com.wuan.UserSystem"
   ```
3. 按照命令行提示输入相关信息进行操作。

## 设计思路

1. 利用 Scanner 类来接收输入，然后用 switch 来根据用户的输入确定执行哪个操作。
2. 如果是注册，则提示输入用户名，程序收到用户名后，先用 usernameExists 方法判断用户名是否存在。如果存在，则提示“重新输入”，如果不存在，
则提示输入密码。对于密码，先用 passwordIsValidate 方法检查密码是否有效，如果无效，则提示
“Username already exists. Please try again.”，如果有效，则调用 registerUser 方法进行注册。
3. 如果是登录，则提示输入用户名和密码，收到这两个字符串后先检查用户名是否存在，如果存在，则检查密码是否正确，如果密码也正确，则输出
"Logged in successfully."；如果密码不正确，则输出"Invalid password."。如果用户名不存在，则输出”Username doesn't exist."。
4. 如果是查询，即调用 listUsers 方法，则程序会遍历 users.xml 文件中的所有 user，并返回一个 ArrayList<String>，该 ArrayList 
包含每个 user 的 username。（该 ArrayList 可能为空）
5. 如果是退出，则把标志位 running 改成 false，这样就可以跳出 while 循环从而结束程序了。

## 依赖

- Java 8
- Maven
- dom4j 2.1.4
- junit-jupiter