package com.mine.java.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-03 19:39
 * @description 数据库编程 - JDBC连接数据库
 *
 * 说明：本机安装 MySQL 8.0.26，且因为是使用 Spring，可以导入相应版本的数据库驱动，运行下面代码即可连接成功。
 * 而且要想成功运行下面代码需要确保 MySQL 的驱动已经成功自动导入。
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest1 {

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
        // 在较新版本的JDBC中，驱动程序的加载和注册是自动完成的，不再需要显式地设置系统属性。
        // 故将下面内容注释掉也可以实现数据库的正常连接，同时，JDBC 会根据 URL 来选择相应的驱动
        // 如果是以“jdbc:mysql”开头的则会选择MySQL驱动，如果是以“jdbc:oracle”开头的则会选择
        // Oracle 驱动。
//        String driver = props.getProperty("jdbc.driver");
//        if (driver != null) {
//            System.setProperty("jdbc.driver", driver);
//        }
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
