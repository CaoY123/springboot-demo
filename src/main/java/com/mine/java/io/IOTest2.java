package com.mine.java.io;

import com.mine.java.io.entity.User;
import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * @author CaoY
 * @date 2023-06-14 16:33
 * @description IO输入输出流学习 - 写入文本到目标文件
 * 下面的各种练习主要演示的是写出使用 PrintWriter 打印流写入文本内容到目标文件。
 * 注意1：这里的文本文件是指我们人类能够看得懂的文件，而不是那种我们难以理解的二进制文件。
 * 注意2：使用输出流的时候，有些高级的输出流本身是自带缓冲区的，当写入内容较少的时候，如果不手动 flush 或关闭流，
 * 缓冲区中的内容时不会刷到目标文件中的，因此一定要在读写文件的时候有关闭流或及时刷新的好习惯。
 */
public class IOTest2 {

    @Test
    public void test1() throws IOException {
        // 输入读入器从控制台读取信息，并将其转换为Unicode（默认编码格式）
        InputStreamReader isr = new InputStreamReader(System.in);

        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "words.txt";
        // 在将从path指示的文件中读入内容并将其转换为指定编码格式的Unicode
        InputStreamReader isr2 = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8);
    }

    // 下面事物均可以成功写入到目标文件，且存入目标文件的是相应编码的字节数据
    @Test
    public void test2() throws FileNotFoundException, UnsupportedEncodingException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "out.txt";
        PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8.name());
        // 字符串
        String content = "This is a simple thing";
        // double 数值
        double d = 3.1415926;
        // BigInteger 对象
        BigInteger bigNum = new BigInteger("12345678987654321234567890");
        // boolean 值
        boolean b = false;
        // User 类对象
        User user = new User(1001, "张三", "男", 22);
        pw.print(content);
        pw.print("\n");
        pw.print(d);
        pw.print("\n");
        pw.print(bigNum);
        pw.print("\n");
        pw.print(b);
        pw.print("\n");
        pw.print(user);
        pw.close();
        System.out.println("============================写入结束============================");
    }

    @Test
    public void test3() throws IOException {
        // 获取系统的行结束符
        String lineSeparator = System.getProperty("line.separator");
        System.out.println("行结束符的长度为：" + lineSeparator.length());

        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "out.txt";
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(path), StandardCharsets.UTF_8),
                true);
        // User 类对象
        User user = new User(1002, "李四", "女", 21);
        // 自动冲刷只是针对 println 而言的，当将自动冲刷设置为true时，只要调用 println，就会写入到目标文件
        // 而默认情况下，自动冲刷是设置为false的。而且，当自动冲刷设置为true时，调用print，也不会自动将内容
        // 写入到目标文件
        pw.println(user);

        // 测试的时候需要把 close 语句注释掉，因为关闭流也会开启刷新，这样就无法进行对照实验了，
        // 但是用完流记得关闭是个好习惯
        pw.close();

        System.out.println("============================写入结束============================");
    }

    @Test
    public void test4() throws FileNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "out.txt";
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(path), StandardCharsets.UTF_8),
                true);
        User user = new User(1003, "王五", "男", 19);
        // 相较print而言，write更原始一些，只能写出基本的数据类型（字符串、字符、数字），而print却可以操作对象
        pw.write(user.toString());
        pw.close();
    }
}
