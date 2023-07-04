package com.mine.java.jdbc;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-04 15:02
 * @description 数据库编程 - 操作 BLOB 对象
 *
 * 前提：
 *  1）需要确保已经导入 MySQL 驱动程序
 *  2）执行 src/main/resources/jdbc/pictures.sql 基本文件创建 pictures 表
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest5 {

    // 向 pictures 表中写入数据
    @Test
    public void test1() throws IOException {
        try (Connection conn = getConnection()) {
            String[] imgNames = {"1.jpg", "2.jpg"};
            Path imgDirPath = Paths.get("src", "main", "resources", "jdbc", "img");

            String insertSql = "INSERT INTO pictures(name, img) VALUES(?, ?)";
            PreparedStatement stat = conn.prepareStatement(insertSql);

            int count = 0;
            for (String imgName : imgNames) {
                Path imgPath = imgDirPath.resolve(imgName);

                byte[] imgData = Files.readAllBytes(imgPath);
                Blob blob = conn.createBlob();
                // 在 JDBC 中，Blob 的索引是从 1 开始的，而不是从 0 开始。因此，
                // 将图片字节数组设置到 Blob 对象时，使用 blob.setBytes(1, imageData) 将字节
                // 数组从索引为 1 的位置开始设置到 Blob 对象中。
                blob.setBytes(1, imgData);

                stat.setString(1, imgName);
                stat.setBlob(2, blob);

                count += stat.executeUpdate();
            }
            System.out.println(count + " rows updated.");
        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        }
    }

    // 查询 BLOB 类型的字段，并使用其生成图片
    @Test
    public void test2() throws IOException {

        try (Connection conn = getConnection()) {

            try (Statement stat = conn.createStatement()) {
                String querySql = "SELECT * FROM pictures";
                try (ResultSet rs = stat.executeQuery(querySql)) {
                    int i = 1;
                    while (rs.next()) {
                        Blob blob = rs.getBlob(3);
                        String name = rs.getString("name");
                        System.out.println(rs.getInt("id") + ", " +
                                 name + ", " + blob);

                        InputStream is = blob.getBinaryStream();
                        String imgName = i + "_" + name;
                        i++;

                        // 从数据库中读取 BLOB 数据并将其写为 序号 + _ + 原图片名字的图片
                        // 例：读取出 1.jpg，读取后生成的图片名称为：1_1.jpg
                        Path imgDirPath = Paths.get("src", "main", "resources", "jdbc", "img");
                        File newImgFile = imgDirPath.resolve(imgName).toFile();
                        try (FileOutputStream fos = new FileOutputStream(newImgFile)) {
                            byte[] buffer = new byte[1024];
                            int bytesCount = 0;
                            while ((bytesCount = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesCount);
                            }
                        }
                    }
                }
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
