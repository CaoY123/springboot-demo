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
 * @date 2023-07-04 19:12
 * @description 数据库编程 - 可滚动和可更新的数据集
 * 下面的练习是针对结果集前后滚动、以及通过结果集来影响数据库记录的一些操作
 *
 * 前提：
 *  1）需要确保已经导入 MySQL 驱动程序
 *  2）执行 src/main/resources/jdbc/pictures.sql 基本文件创建 pictures 表
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest7 {

    // 与 test2 形成对比，不能向前滚动，会抛出一个错误
    @Test
    public void test1() throws IOException {
        try (Connection conn = getConnection()) {
            // 设置查询的结果集为可以前后滚动的且对不能通过结果集修改数据的
            try (Statement stat = conn.createStatement()) {
                String querySql = "SELECT * FROM authors;";
                ResultSet rs = stat.executeQuery(querySql);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i < columnCount; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    System.out.print(metaData.getColumnLabel(i));
                }
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i < columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");
                while (rs.previous()) {
                    for (int i = 1; i < columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    // 与 test1 对比，可前后滚动的数据集，先按查询结果集的正序打印一遍，再按查询结果集的倒序打印一遍，
    // 再将结果集的游标移动后打印输出看效果
    @Test
    public void test2() throws IOException {

        try (Connection conn = getConnection()) {
            // 设置查询的结果集为可以前后滚动的且对不能通过结果集修改数据的
            try (Statement stat = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                String querySql = "SELECT * FROM authors limit 0, 5";
                ResultSet rs = stat.executeQuery(querySql);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i < columnCount; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    System.out.print(metaData.getColumnLabel(i));
                }
                System.out.println();

                // 正序输出
                while (rs.next()) {
                    for (int i = 1; i < columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");

                // 倒序输出
                while (rs.previous()) {
                    for (int i = 1; i < columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");

                // 将结果集的游标向前移动4个
                rs.relative(4);
                for (int i = 1; i < columnCount; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    System.out.print(rs.getString(i));
                }
                System.out.println();
                System.out.println("==============================");

                // 将游标定位到第二个元素上
                rs.absolute(2);
                for (int i = 1; i < columnCount; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    System.out.print(rs.getString(i));
                }
                System.out.println();
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    @Test
    public void test3() throws IOException {

        try (Connection conn = getConnection()) {
            // 可前后滚动且对数据库变化不敏感的、可以修改数据库内容的结果集
            try (Statement stat = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE)) {
                String querySql = "SELECT id, name FROM pictures limit 0, 5;";
                ResultSet rs = stat.executeQuery(querySql);

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                System.out.println("原结果集：");
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    System.out.print(metaData.getColumnLabel(i));
                }
                System.out.println();

                // 正序输出
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();

                    String name = rs.getString("name");
                    // 只是更新结果集中的值，这里修改了一下名字
                    rs.updateString(2, "p" + name);
                    // 将更新信息发送给数据库，同步更新数据库的值，如果没有 updateRow 这句，
                    // 则上面所做的一切对于结果集的修改都会被丢弃，而且数据库的值也不会改变
                    rs.updateRow();
                }
                System.out.println("==============================");

                // 将游标置为结果集第一个元素的前面，因为前面遍历已经移动到了最后一个元素的后面
                rs.beforeFirst();
                System.out.println("修改后：");
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");

                // 添加一条记录
                // 先移动游标到插入行（一个用于插入记录的特定位置）
                rs.moveToInsertRow();
                String insertName = "insertResult" + System.currentTimeMillis() + ".jpg";
                rs.updateString(2, insertName);
                // 确认插入数据并更新数据库
                rs.insertRow();
                // 将游标移动动调用 moveToInsertRow() 之前的行
                rs.moveToCurrentRow();

                rs.beforeFirst();
                System.out.println("插入后：");
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");

                // 移动游标至最后一个查询到的元素之后
                rs.afterLast();
                rs.previous();
                // 删除游标所指的元素，该元素会从数据库和结果集中删除
                rs.deleteRow();

                rs.beforeFirst();
                System.out.println("删除后：");
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(rs.getString(i));
                    }
                    System.out.println();
                }
                System.out.println("==============================");
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

        return DriverManager.getConnection(url,username, password);
    }
}
