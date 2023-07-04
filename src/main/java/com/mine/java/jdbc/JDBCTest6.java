package com.mine.java.jdbc;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-04 16:05
 * @description 数据库编程 - 查询及处理操作
 * 包括：包含下划线的匹配、多个结果集的处理、获取自动生成的主键
 *
 * 前提：
 *  1）需要确保已经导入 MySQL 驱动程序
 *  2）执行 src/main/resources/jdbc/pictures.sql 基本文件创建 pictures 表
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest6 {

    // 下划线的作用的练习
    @Test
    public void test1() throws IOException {

        try (Connection conn = getConnection()) {
            try (Statement stat = conn.createStatement()) {
                String querySql1 = "SELECT * FROM authors where name like '_EL_'";
                ResultSet rs = stat.executeQuery(querySql1);
                while (rs.next()) {
                    System.out.println(rs.getString(1) + ", " + rs.getString(2)
                    + ", " + rs.getString(3));
                }
            }

        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    // 一次执行多个 SQL 查询、更新语句，获得和处理多个结果集
    @Test
    public void test2() throws IOException {

        try (Connection conn = getConnection()) {
            try (Statement stat = conn.createStatement()) {
                String[] commands = {"SELECT Title, ISBN, Publisher_Id FROM books WHERE Publisher_id = '0201';",
                        "SELECT * FROM authors limit 0, 5;",
                        "SELECT * FROM publishers WHERE LENGTH(Name) <= 10;",
                        "UPDATE books SET Price = Price + 1 WHERE Publisher_Id = '0201';"};
                for (String command : commands) {
                    // execute 只能执行单个的 SQL 语句，不能执行多条 SQL 语句，
                    boolean isResult = stat.execute(command);
                    boolean done = false;
                    while (!done) {
                        if (isResult) {
                            // 获取列名并打印
                            ResultSet rs = stat.getResultSet();
                            ResultSetMetaData metaData = rs.getMetaData();

                            int columnCount = metaData.getColumnCount();
                            for (int i = 1; i <= columnCount; i++) {
                                if (i > 1) {
                                    System.out.print(", ");
                                }
                                System.out.print(metaData.getColumnLabel(i));
                            }
                            System.out.println();

                            while (rs.next()) {
                                for (int i = 1; i <= columnCount; i++) {
                                    if (i > 1) {
                                        System.out.print(", ");
                                    }
                                    System.out.print(rs.getString(i));
                                }
                                System.out.println();
                            }
                        } else {
                            int updateCount = stat.getUpdateCount();
                            if (updateCount >= 0) {
                                System.out.println(updateCount + " rows updated");
                            } else {
                                done = true;
                            }
                        }
                        if (!done) {
                            isResult = stat.getMoreResults();// 获取更多的结果，必须加，如果没有，则无法退出循环
                        }
                    }
                    System.out.println("=============================");
                }
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    // 获取自动生成的主键，需要提前运行 src/main/resources/jdbc/pictures.sql 脚本文件建表 pictures.sql
    @Test
    public void test3() throws IOException {

        try (Connection conn = getConnection()) {
            try (Statement stat = conn.createStatement()) {
                String insertSql = "INSERT INTO pictures(name) VALUES('test.jpg');";

                int insertCount = stat.executeUpdate(insertSql, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = stat.getGeneratedKeys();
                if (rs.next()) {
                    int key = rs.getInt(1);
                    System.out.println("key: " + key);
                }
                System.out.println(insertCount + " rows updated.");
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws IOException, SQLException {
        Properties props = new Properties();
        Path propsPath = Paths.get("src", "main", "resources",
                "jdbc", "database1.properties");
        try (InputStream in = Files.newInputStream(propsPath)) {
            props.load(in);
        }

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }
}
