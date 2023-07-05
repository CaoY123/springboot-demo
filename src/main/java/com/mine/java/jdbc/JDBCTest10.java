package com.mine.java.jdbc;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-05 14:07
 * @description 数据库编程 - 事务
 *
 * 几个操作要么一起成功顺利提交，要么中途失败回滚到初始状态
 *
 * 下面的代码练习了事务的回滚操作，通过自己在其中故意制造错误而造成回滚，并观察了有无回滚的效果。
 *
 * 前提：
 *  1）需要确保已经导入 MySQL 驱动程序
 *  2）执行 src/main/resources/jdbc/pictures.sql 基本文件创建 pictures 表
 *
 * 注意：确保配置正确自己的数据库基本信息，包括：url、驱动类名、用户名和密码。
 */
public class JDBCTest10 {

    // 回滚的练习
    @Test
    public void test1() throws IOException {

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);// 开启事务，关闭自动提交，因为在自动提交开启时无法进行回滚操作

            ResultSet rs = null;
            Statement stat = conn.createStatement();
            String querySql = "SELECT id, name FROM pictures;";
            rs = stat.executeQuery(querySql);
            String updateSql = "UPDATE pictures SET name = ? WHERE id = ?";
            PreparedStatement pstat = conn.prepareStatement(updateSql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                pstat.setString(1, "r" + name);
                pstat.setInt(2, id);
                pstat.executeUpdate();
            }

            // 专门制造的异常，用于测试回滚的效果
            int num = 5 / 0;

            System.out.println("更新成功！");
        } catch (Exception e) {
            System.out.println("发生异常：" + e + "，回滚");
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    // 确保提交操作一定进行，不会受发生异常的干扰
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用保存点的例子，确保 pictures 表里面有两条 id 分别为1、2的记录
     * 将保存点设置在第一条记录已经更新但是第二条记录未更新之间，回滚后数据库中第一条记录
     * 已经更新但是第二条记录未更新，如果按照默认回滚则回滚到事务开始处，则数据库中两条记录都没有更新
     */
    @Test
    public void test2() {
        Connection conn = null;
        Savepoint savepoint1 = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);// 开启事务，关闭自动提交，因为在自动提交开启时无法进行回滚操作

            String updateSql = "UPDATE pictures SET name = ? WHERE id = ?";
            PreparedStatement pstat = conn.prepareStatement(updateSql);

            // 更新第一条记录
            // 获取当前时间戳，用于更新名字，使得名字为 一个小写字母 + .jpg
            BigInteger bi = BigInteger.valueOf(System.currentTimeMillis()).mod(new BigInteger("26"));
            char ch = (char)('a' + bi.intValue());

            pstat.setString(1, ch + ".jpg");
            pstat.setInt(2, 1);
            pstat.executeUpdate();

            // 设置一个保存点，位于已经将第一条记录更新但是还未更新第二条记录
            savepoint1 = conn.setSavepoint("savepoint1");

            // 更新第二天记录
            bi = BigInteger.valueOf(System.currentTimeMillis()).mod(new BigInteger("26"));
            ch = (char)('a' + bi.intValue());
            pstat.setString(1, ch + ".jpg");
            pstat.setInt(2, 2);
            pstat.executeUpdate();

            // 专门制造的异常，用于测试回滚的效果
            int num = 5 / 0;

            System.out.println("更新成功！");
        } catch (Exception e) {
            System.out.println("发生异常：" + e + "，回滚");
            e.printStackTrace();
            try {
                if (conn != null) {
                    // 回滚到保存点，如果不传递保存点参数，则会默认回滚到 conn.setAutoCommit(false);
                    // 即开启事务处。
                    conn.rollback(savepoint1);
                }
                if (savepoint1 != null) {
                    // 释放保存点
                    conn.releaseSavepoint(savepoint1);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    // 确保提交操作一定进行，不会受发生异常的干扰
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 批量更新，批量更新时中的每个 SQL 必须是单一的 SQL 语句，而不是由分号分割的实际上为多个 SQL 的语句。
     * 注意：必须将批量执行的操作视为事务，如果它在执行过程中有一个操作失败，则应该恢复到执行之前的状态。
     */
    @Test
    public void test3() {

        Connection conn = null;
        boolean autoCommit = false;
        try {
            conn = getConnection();
            // 这里要保存原来的提交状态，不一定为 true，这种编码习惯较好
            autoCommit= conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (Statement stat = conn.createStatement()) {
                String[] insertSqls = {"INSERT INTO pictures(name) VALUES('6.jpg');" ,
                        "INSERT INTO pictures(name) VALUES('7.jpg');" ,
                        "INSERT INTO pictures(name) VALUES('8.jpg');" ,
                        "INSERT INTO pictures(name) VALUES('9.jpg');" ,
                        "INSERT INTO pictures(name) VALUES('10.jpg');"};

                for (String insertSql : insertSqls) {
                    stat.addBatch(insertSql);
                }
                // 提交整个批量更新语句，返回一个记录数的数组
                int[] array = stat.executeBatch();
                System.out.println(Arrays.toString(array));
            }
        } catch (Exception e) {
            System.out.println("发生错误：" + e + "，回滚");
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.commit();
                    conn.setAutoCommit(autoCommit);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
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
