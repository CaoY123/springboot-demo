package com.mine.java.io;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author CaoY
 * @date 2023-06-16 10:55
 * @description IO流学习 - 读写二进制数据
 * 下面练习展示了向 bin.txt 中写入二进制数据，接着从 bin.txt 中读取刚写入的数据的操作。
 * 需要秉持的原则：怎么写入的，就要怎么读出来。
 *
 * writeUTF：在写入字符串时，会先写入一个短整型（2个字节）的数据来表示后面要写入的字节数，然后再写入实际的字符串数据。
 * 而 writeChars 和 writeBytes 则并不会记录一次写写入的字节的个数。所以writeUTF显然要更加强大，但是其效率相比
 * writeChars 和 writeBytes 要低一些。
 *
 * 写入包含中文字符的字符串时，使用 writeChars() 写入时能读取正确的结果，而用 writeBytes() 时则不行。
 * 此外，使用 writeUTF() 写入包含中文的字符串时，用 readUTF() 读取时能取得正确的结果。而且，由 writeUTF()
 * 写入的字符串可以被人直接阅读。
 */
public class IOTest6 {

    // 向 bin.txt 中用 DataOutputStream 写入数据
    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "bin.txt";
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(path));
        dos.write(5);// 直接写入整数
        dos.write('\n');// 写入换行符
        dos.writeInt(30);// 调用专门写入整数的方法
        dos.write('\n');
        dos.writeBytes("abcde");// 调用写入字符串为二进制数组的方法
        dos.write('\n');
        dos.writeLong(123456789012345L);// 调用专门写入 long 类型数据的方法
        dos.write('\n');
        dos.writeChars("Hello, World!");// 调用写入字符串为 字符数组的方法
        dos.write('\n');
        dos.writeChar(98);// 调用传入 ASCII 值写入对应字符的方法
        dos.write('\n');
        dos.writeBoolean(false);// 调用专门写入 boolean 类型数据的方法
        dos.write('\n');
        dos.writeByte(127);// 需要确保写入的范围在 [-128, 127] 中，否则会出现读取的值与写入的值不一致
        dos.write('\n');
        dos.writeUTF("I am a boy, so I want to get a girlfriend.");// 调用 writeUTF()
        dos.write('\n');
        dos.write(new byte[]{1, 3, 5, 7, 12, 16, 18});// 调用写入 byte 数组的方法
        dos.write('\n');
        dos.writeChars("你好Java");// 写入中文，用 char 写入读取的时候可以读出正确的结果，而用 byte 不行
        dos.write('\n');
        dos.writeUTF("你好Java");
        dos.close();
        System.out.println("写入 bin.txt 完成");
    }

    // 从 bin.txt 中读取 test1 中写入的数据。从下面的练习中可以总结出：
    // 怎么写入的，就要怎么读取出来，比如：写入的时候调用的是 write(5)，则读取的时候要调用 read()
    // 才能读取出5来；而写入的时候调用writeInt(30)，读取的时候调用 readInt() 才能读取出来30。
    // 否则的话就会读取出乱码来。
    @Test
    public void test2() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "bin.txt";
        DataInputStream dis = new DataInputStream(new FileInputStream(path));

        int a = dis.read();
        System.out.println("a = " + a);

        int b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        int c = dis.readInt();
        System.out.println("(int)c = " + c);// 读取出整数30

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        // 这里读取的时候需要指定读取的字符的个数，否则如果读取的数字过大的话会报一个EOFException错误，实际上就是按照
        // 数组的长度来读取的，报出EOFException是说明超出文件内容的界限了。故读取的时候需要确保不要超出这个文件内容的边界
        byte[] buffer = new byte[5];
        dis.readFully(buffer);
        String str = new String(buffer, StandardCharsets.UTF_8);
        System.out.println("str = " + str);

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        long d = dis.readLong();
        System.out.println("(long)d = " + d);

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        // 逐个将写入的字符读取出来，也需要确定读取的字符个数
        char[] chars = new char[13];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = dis.readChar();
        }
        System.out.println("chars = " + Arrays.toString(chars));

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        char e = dis.readChar();
        System.out.println("(char)e = " + e);

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        boolean f = dis.readBoolean();
        System.out.println("(boolean)f = " + f);

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        byte g = dis.readByte();
        System.out.println("(byte)g = " + g);

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        String utfStr = dis.readUTF();
        System.out.println("utfStr = " + utfStr);

        int skip = dis.skipBytes(1);// 跳过字节个数，返回实际跳过的字符个数
        System.out.println("skip = " + skip);

        byte[] bytes = new byte[7];
        int count = dis.read(bytes);
        System.out.println("bytes = " + Arrays.toString(bytes));
        System.out.println("bytes_count = " + count);

        b = dis.read();
        System.out.println("(ASCII)b = " + b);// 换行键的ASCII值

        char[] chars1 = new char[6];
        for (int i = 0; i < chars1.length; i++) {
            chars1[i] = dis.readChar();
        }
        String str1 = new String(chars1);
        System.out.println("str1 = " + str1);

        dis.skipBytes(1);
        String str2 = dis.readUTF();
        System.out.println("str2 = " + str2);

        // 读取到末尾再读取返回的值依然是-1，不会进一步报错。
        int eof = dis.read();
        System.out.println("eof = " + eof);

        int eof2 = dis.read();
        System.out.println("eof2 = " + eof2);

        dis.close();
    }
}
