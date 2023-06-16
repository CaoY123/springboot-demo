package com.mine.java.io;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author CaoY
 * @date 2023-06-12 14:03
 * @description IO学习1 - 输入输出流基础
 *
 * 输入输出流的创建和基本结构的认识。Java定义了丰富的输入输出流的操作，与其他语言不同的是，Java的输入输出流在
 * 使用的时候需要组合。具体的组合策略为：将基础功能的输入输出流组合到高级功能的输入输出流上去。通常基础的输入输出流
 * 位于父类的级别上，而更高级的输入输出流位于子类的级别上。Java这么做可以使我们开发者更方便地组合输入输出流。
 *
 */
public class IOTest1 {

    @Test
    public void test1() throws IOException {
        InputStream is = new FileInputStream("src/main/resources/files/words.txt");
        int availableCount = is.available();
        System.out.println("可读取的字节数为：" + availableCount);
        is.close();
    }

    @Test
    public void test2() throws IOException {
        // 获取用户当亲的工作目录
        String userDir = System.getProperty("user.dir");
        System.out.println("用户工作目录：" + userDir);

        // 通过 File.separator 来确定系统的文件分隔符，可以适应各种系统的要求，提高代码的可移植性
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "words.txt";
        InputStream is = new FileInputStream(path);
        int availableCount = is.available();
        System.out.println("可读取的字节数为：" + availableCount);
        is.close();
    }

    @Test
    public void test3() throws FileNotFoundException {
        // FileInputStream 负责从文件和其他外部位置获取字节，而其他的输入流（例如DataInputStream）可以将字节组装到
        // 更有用的数据类型中，我们必须对二者进行组合，具体的方法为：将提供基础功能的流（如FileInputStream）组装到提供
        // 跟高级功能的流（如DataInoutStream）中，提供基础功能的输入输出流的类通常位于父类级别，而提供更高级功能的输入
        // 输出流的类通常位于子类级别，这符合子类对于父类功能拓展的这一设计原则。
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "words.txt";
        FileInputStream fis = new FileInputStream(path);
        DataInputStream dis = new DataInputStream(fis);
    }

    @Test
    public void test4() throws IOException {
        // 给 test3 中的例子加上缓冲区的功能
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "words.txt";
        FileInputStream fis = new FileInputStream(path);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);

        // 上述做法相当于把多个流链接到一起，我们需要跟踪各个中介输入流
        PushbackInputStream pbis = new PushbackInputStream(dis);
        int aChar = pbis.read();
        System.out.println("读入的一个字符是：" + aChar);
        if (aChar != 'a') {
            // 将读入的值推回流中
            pbis.unread(aChar);
        }
        int bChar = pbis.read();
        // 相等，说明确实发生了推回操作
        System.out.println(("aChar 等于 bChar 吗？" + (aChar == bChar ? "是" : "否")));
    }

    @Test
    public void test5() throws IOException {
        // 覆盖式写入
        FileOutputStream fos = new FileOutputStream("src" + File.separator +
                "main" + File.separator + "resources" + File.separator + "files"
                + File.separator + "out.txt");

        String content = "Hello, Java World!";
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        fos.write(contentBytes);
        System.out.println("写出操作结束");
        fos.close();

        // 追加式写入
        FileOutputStream fos2 = new FileOutputStream("src" + File.separator +
                "main" + File.separator + "resources" + File.separator + "files"
                + File.separator + "out.txt", true);
        fos2.write(contentBytes);
    }
}
