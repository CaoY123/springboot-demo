package com.mine.java.io;

import com.mine.java.io.entity.User;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;

/**
 * @author CaoY
 * @date 2023-06-19 11:15
 * @description IO流学习 - 对象序列化的文件格式
 * 下面的一个测试用例向 obj.dat 中写入了一个 User 对象，
 * 按照《Java核心技术卷2》的第2.3.2的解析序列化后的内容，我逐一对其试图解析，基本明白了很多东西，虽然还是有些东西不理解。
 * 但是基本明白了如下核心内容：
 * 1. 对象流输出中包含所有对象的类型和数据域；
 * 2. 每个对象都被赋予一个序列号；
 * 3. 相同对象的重复出现将被存储为对这个对象的序列号的引用。
 */
public class IOTest9 {

    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        User user = new User(1001, "LiHua", "man", 32, LocalDate.of(1992, 3, 17));
        oos.writeObject(user);

        oos.close();
        System.out.println("==========================程序运行结束==========================");
    }
}
