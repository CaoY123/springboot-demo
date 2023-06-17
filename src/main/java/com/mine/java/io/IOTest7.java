package com.mine.java.io;

import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.*;


/**
 * @author CaoY
 * @date 2023-06-16 16:10
 * @description IO流学习 - 随机访问文件
 * 下面练习展示了使用 RandomAccessFile 进行文件随机访问和进行关于 zip 的基础操作。
 */
public class IOTest7 {

    // 随机访问文件 - RandomAccessFile
    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "bin2.txt";
        RandomAccessFile in = new RandomAccessFile(path, "r");
        RandomAccessFile inOut = new RandomAccessFile(path, "rw");

        inOut.writeUTF("I have a good idea about the future.");
        String utfStr = in.readUTF();
        System.out.println("utfStr = " + utfStr);

        inOut.write('\n');

        // 将文件指针设置到最开始的位置
        inOut.seek(0);
        String utfStr2 = inOut.readUTF();
        System.out.println("utfStr2 = " + utfStr2);

        // 需要明确，writeUTF 会在最开始用两个字节描述后面一次性写入的字符个数
        inOut.seek(39);
        inOut.writeDouble(3.1415926);
        inOut.writeChars("我爱Java语言");

        int b = in.read();
        System.out.println("(ASCII)b = " + b);

        // 获得 in 读取流的当前指针的位置
        long inPointer1 = in.getFilePointer();
        System.out.println("inPointer1 = " + inPointer1);

        // 获取 inOut 读取写入流当前指针的位置
        long inOutPointer1 = inOut.getFilePointer();
        System.out.println("inOutPointer1 = " + inOutPointer1);

        double c = in.readDouble();
        System.out.println("(double)c = " + c);

        char[] chars = new char[8];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = in.readChar();
        }
        String str = new String(chars);
        System.out.println("str = " + str);

        // 获取文件的字节总数
        long inLength = in.length();
        long inOutLength = inOut.length();
        System.out.println("inLength = " + inLength);
        System.out.println("inOutLength = " + inOutLength);
    }

    // zip文档，读取zip文件
    @Test
    public void test2() throws IOException {
        // files.zip 压缩了之前几个 IO流学习的文件
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "files.zip";
        ZipInputStream zis = new ZipInputStream(new FileInputStream(path));
        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println(entry);
            zis.closeEntry();
        }
        zis.close();
    }

    // zip文档，写入zip文件
    @Test
    public void test3() throws IOException {
        // 将这个文件夹下的所有 txt 文件进行 zip 格式的压缩
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files";
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
                path + File.separator + "test.zip"));
        File directory = new File(path);
        String[] files = directory.list();
        System.out.println("files: " + Arrays.toString(files));
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith("txt")) {
                ZipEntry entry = new ZipEntry(file.getName());
                // ZipEntry 还有一些属性的设置方法，这里不作具体展示
                zos.putNextEntry(entry);
                zos.closeEntry();
            }
        }
        zos.close();
    }

    // 关于 zip 文件的其他操作
    @Test
    public void test4() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "files.zip";
        ZipInputStream zis = new ZipInputStream(new FileInputStream(path));
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            long crc = entry.getCrc();
            System.out.println("该zip项的CRC32校验和的值：" + crc);

            String name = entry.getName();
            System.out.println("该zip向的名字：" + name);

            long size = entry.getSize();
            System.out.println("该zip项的尺寸：" + size);// 未压缩前的尺寸

            boolean isDirectory = entry.isDirectory();
            System.out.println("该zip项是否是目录：" + (isDirectory ? "是" : "否"));

            System.out.println("=============================================");
            zis.closeEntry();
        }
        zis.close();
    }

    @Test
    public void test5() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "test.zip";
        ZipFile zipFile = new ZipFile(path);
        System.out.println("压缩文件名：" + zipFile.getName());
        System.out.println("压缩的文件数目：" + zipFile.size());
        System.out.println("===========================================");
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            System.out.println(entry);
        }
    }
}
