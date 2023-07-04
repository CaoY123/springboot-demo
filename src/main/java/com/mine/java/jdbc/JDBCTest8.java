package com.mine.java.jdbc;

import org.junit.Test;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-04 20:43
 * @description 数据库编程 - 行集
 * 可滚动结果集的问题：可滚动的结果集虽然功能强大，但是却有一个重要的缺陷，就是在与用户整个交互的过程中，
 * 必须始终与数据库保持连接。用户也许会离开电脑很长一段时间，但是在此期间却始终占有者数据库连接，这显然是不合适的。
 * 我们使用 行集 来解决上述问题，它无序保持与数据库的连接。
 *
 * 下面的练习演示了行集的部分使用，更详细的使用可查阅具体的 API 进行。
 *
 * 前提：
 *  1）需要确保已经导入 MySQL 驱动程序
 *  2）执行 src/main/resources/jdbc/pictures.sql 基本文件创建 pictures 表
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest8 {

    // 结果集 ResultSet 断开数据库连接后的操作，在关闭数据库连接后任何对结果集的操作都是无效的，会报错。
    @Test
    public void test1() throws IOException {
        try {
            Connection conn = getConnection();

            try (Statement stat = conn.createStatement()) {
                String querySql = "SELECT id, name FROM pictures";
                ResultSet rs = stat.executeQuery(querySql);

                conn.close();// 手动关闭数据库连接，已验证断开后 行集 能否发挥作用

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
                System.out.println("=================================");


                conn = getConnection();// 再次获得数据库连接
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    // 在关闭结果集后仍然能对行集操作
    @Test
    public void test2() throws IOException {
        try {
            Connection conn = getConnection();
            try (Statement stat = conn.createStatement()) {
                String querySql = "SELECT id, name FROM pictures";
                ResultSet rs = stat.executeQuery(querySql);

                RowSetFactory factory = RowSetProvider.newFactory();
                CachedRowSet crs = factory.createCachedRowSet();

                crs.populate(rs);
                conn.close();// 手动关闭数据库连接，已验证断开后 行集 能否发挥作用

                ResultSetMetaData metaData = crs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    if (i > 1) {
                        System.out.print(", ");
                    }
                    System.out.print(metaData.getColumnLabel(i));
                }
                System.out.println();

                while (crs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        if (i > 1) {
                            System.out.print(", ");
                        }
                        System.out.print(crs.getString(i));
                    }
                    System.out.println();

                    String name = crs.getString("name");
                    // 更新原有的 name
                    crs.updateString(2, "k" + name);
                    crs.updateRow();
                }
                System.out.println("=================================");

                conn = getConnection();// 再次获得数据库连接
                conn.setAutoCommit(false);
                crs.acceptChanges(conn);
                conn.close();
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    public static Connection getConnection () throws IOException, SQLException {
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
