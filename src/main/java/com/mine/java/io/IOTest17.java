package com.mine.java.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-06-25 13:57
 * @description IO流学习 - 内存映射文件
 * 下面的练习介绍了内存映射文件，以及使用缓冲区来读取和写入数据的部分基础操作，这是一种比较高效的方式。
 */
public class IOTest17 {

    // 使用 FileChannel 的缓冲区读取内容
    @Test
    public void test1() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "words.txt");

        List<String> wordList = new ArrayList<>();
        try (FileChannel fileChannel = FileChannel.open(path)) {
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(path));
            byte[] bytes = new byte[1024];
            int size = 0;
            for (int i = 0; i < byteBuffer.limit(); i++) {
                bytes[size] = byteBuffer.get();
                if (bytes[size] == 13 || i == byteBuffer.limit() - 1) {
                    if (i != byteBuffer.limit() - 1) {
                        byteBuffer.get();
                        i++;// 因为在windows10系统中，回车符是 \r\n
                    } else {
                        size++;
                    }
                    String s = new String(bytes, 0, size);
                    wordList.add(s);
                    size = 0;
                } else {
                    size++;
                }
            }
            wordList.forEach(System.out::println);
        }
    }

    @Test
    public void test2() throws IOException {
        Path path = Paths.get("src", "main", "resources", "files", "bin4.txt");
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
            byteBuffer.putInt(123);

            byteBuffer.force();// 将缓冲区的更改强制刷新到磁盘上去

            // flip()：切换至读模式，必需的，没有这句会造成读写错误，flip 是将界限设置到当前位置，并将位置复位到0，
            // 使缓冲区为读取数据做好准备
            byteBuffer.flip();
            int intNum = byteBuffer.getInt();
            System.out.println("intNum = " + intNum);

            // clear()：将位置设置为0，并将界限设置到容量，为缓冲区的写入数据做好准备
            byteBuffer.clear();
            byteBuffer.putChar('A');
            byteBuffer.force();

            byteBuffer.flip();// 切换至读模式
            char aChar = byteBuffer.getChar();
            System.out.println("aChar = " + aChar);
        }
    }
}
