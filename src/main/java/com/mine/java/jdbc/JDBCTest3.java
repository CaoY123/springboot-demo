package com.mine.java.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-03 23:30
 * @description 数据库编程 - 使用 JDBC 语句
 *
 * 需要确保导入了MySQL驱动
 *
 * 下面的练习就是将 Authors.sql、Books.sql、BooksAuthors.sql、Publishers.sql
 * 以代码的形式完成执行并查询各自表中的数据并打印输出。当然，可以通过手动执行SQL脚本在相应
 * 的数据库上建表，不过，我想在此处练习一下 JDBC 的原始操作。
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest3 {

    public static void main(String[] args) throws IOException {
        Path sqlDirPath = Paths.get("src", "main", "resources", "jdbc");

        String[] sqlFileNames = {"Authors.sql", "Books.sql", "BooksAuthors.sql", "Publishers.sql"};

        for (String sqlFileName : sqlFileNames) {
            Path sqlPath = sqlDirPath.resolve(sqlFileName);

            try (Scanner in = new Scanner(sqlPath, StandardCharsets.UTF_8.name())) {

                try (Connection conn = getConnection();
                    Statement stat = conn.createStatement()) {

                    while (in.hasNextLine()) {
                        String line = in.nextLine().trim();
                        if (line.endsWith(";")) {
                            line = line.substring(0, line.length() - 1);
                        }

                        boolean isResult = stat.execute(line);
                        if (isResult) {
                            try (ResultSet rs = stat.getResultSet()) {
                                showResultSet(rs);
                            }
                        } else {
                            int updateCount = stat.getUpdateCount();
                            System.out.println(updateCount + " rows updated");
                        }
                    }
                } catch (SQLException e) {
                    for (Throwable t : e) {
                        t.printStackTrace();
                    }
                }

            }
        }

    }

    /**
     * 获取数据库的连接
     * @return
     * @throws IOException
     * @throws SQLException
     */
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

    /**
     * 打印结果集
     * @param result
     * @throws SQLException
     */
    public static void showResultSet(ResultSet result) throws SQLException {
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 获取列名并打印在一行上
        for (int i = 1; i < columnCount; i++) {
            if (i > 1) {
                System.out.print(", ");
            }
            System.out.print(metaData.getColumnLabel(i));
        }
        System.out.println();

        // 获取每行的属性值
        while (result.next()) {
            for (int i = 1; i < columnCount; i++) {
                if (i > 1) {
                    System.out.print(", ");
                }
                System.out.print(result.getString(i));
            }
            System.out.println();
        }
    }
}
