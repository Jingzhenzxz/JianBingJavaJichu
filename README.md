# XML Login Register

一个使用 Java 和 dom4j/XML 实现的简易命令行登录注册系统。

## 功能

- 注册用户
- 用户登录
- 查询用户数量和信息
- 退出程序

## 用法

1. 克隆本仓库到本地。
2. 使用 Maven 编译和运行项目，或使用您喜欢的 IDE 导入项目并运行。

```bash
$ git clone https://github.com/yourusername/UserSystem.git
$ cd UserSystem
$ mvn compile exec:java -Dexec.mainClass="com.wuan.UserSystem"
```
3. 按照命令行提示输入相关信息进行操作。

## 设计思路

本项目使用 Java 和 dom4j 库来实现 XML 文件的读取和写入。用户信息以 XML 格式存储在 "users.xml" 文件中。

## 注册

当用户选择注册时，程序将提示用户输入用户名和密码。然后，系统会检查用户名是否已存在，以及密码是否符合安全要求（至少包含一个字母、一个数字、一个特殊字符，且长度大于等于6个字符）。如果验证通过，用户信息将被添加到 XML 文件中，并提示用户注册成功。

## 登录

当用户选择登录时，程序将提示用户输入用户名和密码。系统会检查 XML 文件中是否存在与输入信息匹配的用户。如果找到匹配的用户，程序将提示登录成功；否则，提示登录失败。

## 查询用户数量和信息

当用户选择查询用户数量和信息时，程序将读取 XML 文件并输出所有用户的用户名以及用户总数。

## 退出

当用户选择退出时，程序将终止运行。

## 依赖

- Java 8+
- Maven
- dom4j 2.1.3

现在您可以根据修改后的 README.md 更新您的项目文档。