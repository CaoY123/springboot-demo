package com.mine.java.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-04 12:14
 * @description 数据库编程 - 执行查询操作
 *
 * 前提：需要确保已经导入 MySQL 数据库驱动。
 *
 * 下面的练习通过列出表格中的作者名和出版社名，通过选择序号（包括Any（所有）以及具体的某个作者或出版社），
 * 来查询书籍名以及对应的价格；还可以选择一次性修改某个出版社下所有书籍的价格。
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest4 {

    public static void main(String[] args) throws IOException {
        try (Connection conn = getConnection()) {
            in = new Scanner(System.in);
            authors.add("Any");
            publishers.add("Any");

            try (Statement stat = conn.createStatement()) {
                String query = "SELECT Name FROM Authors";
                try (ResultSet rs = stat.executeQuery(query)) {
                    while (rs.next()) {
                        authors.add(rs.getString("name"));
                    }
                }

                query = "SELECT Name FROM publishers";
                try (ResultSet rs = stat.executeQuery(query)) {
                    while (rs.next()) {
                        publishers.add(rs.getString("Name"));
                    }
                }
            }

            boolean done = false;
            while (!done) {
                System.out.println("Q(查询)，C（改变），E（退出）：");
                String input = in.next().toUpperCase();
                if ("Q".equals(input)) {
                    executeQuery(conn);
                } else if ("C".equals(input)) {
                    changePrices(conn);
                } else {
                    done = true;
                }
            }
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    // 查询所有书本的价格，给出书名和相应的价格
    private static final String allQuery = "SELECT books.Price, books.Title FROM books;";

    // 根据作者名和出版社名查询相应的书名和价格，需要连接4个表
    private static final String authorPublisherQuery = "SELECT Books.Price, Books.Title " +
            "FROM Books, BooksAuthors, Authors, Publishers " +
            "WHERE Authors.Author_Id = BooksAuthors.Author_Id " +
            "AND BooksAuthors.ISBN = Books.ISBN " +
            "AND Books.Publisher_Id = Publishers.Publisher_Id " +
            "AND Authors.Name = ? " +
            "AND Publishers.Name = ?;";

    // 根据作者名查询相应书籍的书籍名和价格，需要连接3个表
    private static final String authorQuery = "SELECT Books.Price, Books.Title " +
            "FROM Books, BooksAuthors, Authors " +
            "WHERE Authors.Author_Id = BooksAuthors.Author_Id " +
            "AND BooksAuthors.ISBN = Books.ISBN " +
            "AND Authors.Name = ?";

    // 根据出本社名查询相应书籍的书籍名和价格，需要连接两个表
    private static final String publisherQuery = "SELECT Books.Price, Books.Title " +
            "FROM Books, Publishers " +
            "WHERE Books.Publisher_Id = Publishers.Publisher_Id " +
            "AND Publishers.Name = ?";

    // 根据出版社名来在书籍当前价格的基础上修改价格，一次性修改该出本社下所有书籍的价格
    private static final String priceUpdate = "UPDATE Books SET Price = Price + ? " +
            "WHERE Books.Publisher_Id = " +
            "(SELECT Publisher_Id " +
            "FROM Publishers " +
            "WHERE Name = ?)";

    private static Scanner in;
    private static List<String> authors = new ArrayList<>();
    private static List<String> publishers = new ArrayList<>();

    private static void executeQuery(Connection conn) throws SQLException {
        String author = select("Authors:", authors);
        String publisher = select("Publishers:", publishers);
        PreparedStatement stat;
        if (!"Any".equals(author) && !"Any".equals(publisher)) {
            stat = conn.prepareStatement(authorPublisherQuery);
            stat.setString(1, author);
            stat.setString(2, publisher);
        } else if (!"Any".equals(author) && "Any".equals(author)) {
            stat = conn.prepareStatement(authorQuery);
            stat.setString(1, author);
        } else if ("Any".equals(author) && !"Any".equals(author)) {
            stat = conn.prepareStatement(publisherQuery);
            stat.setString(1, publisher);
        } else {
            stat = conn.prepareStatement(allQuery);
        }

        try (ResultSet rs = stat.executeQuery()) {
            if (rs.getFetchSize() == 0) {
                System.out.println("查询的结果为空");
            }
            while (rs.next()) {
                System.out.println(rs.getString(1) + ", " + rs.getString(2));
            }
        }

    }

    public static void changePrices(Connection conn) throws SQLException {
        String publisher = select("Publishers:", publishers.subList(1, publishers.size()));
        System.out.println("Change prices by: ");

        double priceChange = in.nextDouble();
        PreparedStatement stat = conn.prepareStatement(priceUpdate);
        stat.setDouble(1, priceChange);
        stat.setString(2, publisher);
        int updateCount = stat.executeUpdate();
        System.out.println(updateCount + "rows updated.");
    }

    public static String select(String prompt, List<String> options) {
        while (true) {
            System.out.println(prompt);
            for (int i = 0; i < options.size(); i++) {
                System.out.printf("%2d) %s%n", i + 1, options.get(i));
            }
            System.out.print("请输入要查询的序号：");
            int sel = in.nextInt();
            if (sel > 0 && sel <= options.size()) {
                return options.get(sel - 1);
            }
        }
    }

    /**
     * 获取数据库连接
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
}
