package com.mine.java.jdbc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-03 20:18
 * @description 手动导入数据库驱动包的练习
 *
 * 前提：运行需要注释掉 pom.xml 文件中的 MySQL 连接的依赖，即在 MySQL 驱动包未自动导入的前提下。
 * 连接数据库后的操作与 JDBCTest1 的一致，只不过 JDBCTest1 为自动导入相应数据库驱动，
 * 而 JDBCTest2 为手动导入相应数据库驱动。
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest2 {

    public static void main(String[] args) throws IOException {
        try {
            runTest();
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    public static void runTest() throws SQLException, IOException {
        try (Connection conn = getConnection();
             Statement stat = conn.createStatement()) {
            stat.executeUpdate("CREATE TABLE Greetings (Message CHAR(20));");
            stat.executeUpdate("INSERT INTO Greetings VALUES ('Hello, World!');");

            try (ResultSet result = stat.executeQuery("SELECT * FROM Greetings;")) {
                if (result.next()) {
                    System.out.println(result.getString(1));
                }
            }

            stat.executeUpdate("DROP TABLE Greetings;");
        }
    }

    public static Connection getConnection() throws IOException, SQLException {
        Properties props = new Properties();
        Path propsPath = Paths.get("src", "main", "resources", "jdbc", "database1.properties");
        try (InputStream in = Files.newInputStream(propsPath)) {
            props.load(in);
        }

        // 可以根据 URL 找到相应的驱动类，故无需设置系统的 jdbc.url 参数
//        String driver = props.getProperty("jdbc.driver");
//        if (driver != null) {
//            System.setProperty("jdbc.driver", driver);
//        }

        // 将驱动程序的路径转换为URL：首先，根据提供的驱动程序jar文件路径创建一个File对象。
        // 然后，使用toURI()方法将文件对象转换为URI，并使用toURL()方法将URI转换为URL。
        // 这样我们就得到了驱动程序的URL，后续需要将其添加到类加载器中。
        File driverFile = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "jdbc" + File.separator +
                "mysql-connector-java-8.0.30.jar");
        URL driverUrl = driverFile.toURI().toURL();

        // 获取系统类加载器：通过ClassLoader.getSystemClassLoader()方法获取应用程序的系统类加载器。
        // 系统类加载器用于加载应用程序的类和资源。
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        try {
            // 反射调用addURL()方法：通过反射，我们在类加载器上调用addURL()方法，将驱动程序的URL添加到类加载器中。
            // addURL()方法是URLClassLoader类中的一个私有方法，它允许我们动态地向类加载器添加新的URL。
            Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURLMethod.setAccessible(true);
            addURLMethod.invoke(classLoader, driverUrl);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Failed to add MySQL driver to classpath", e);
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
