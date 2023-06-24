package com.mine.java.io;

import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author CaoY
 * @date 2023-06-23 15:08
 * @description IO流学习 - Path 的相关操作
 */
public class IOTest15 {

    // 注：路径不必对应这某个真实存在的文件，它仅仅是一个抽象的名字序列。
    @Test
    public void test1() {
        // 绝对路径，会自己拼接默认文件系统的分隔符
        Path absolutePath = Paths.get("M:", "Study_documents");
        System.out.println("绝对路径：" + absolutePath);

        // 相对路径
        Path relativePath = Paths.get("src", "main", "java", "com", "mine", "java", "io");
        System.out.println("相对路径：" + relativePath);

        // 获取用户工作路径
        String userDir = System.getProperty("user.dir");
        Path userPath = Paths.get(userDir);
        System.out.println("用户工作路径（绝对路径）：" + userPath);

        // p.resolve(q) 规则：
        // 如果q是绝对路径，则结果就是q
        // 否则，则根据文件系统的规则，将p后面跟着q作为结果
        // 所以 resolve() 可视为一种组合文件路径的操作
        Path resolvedUserPath = userPath.resolve(userPath);
        System.out.println("解析过的绝对路径：" + resolvedUserPath);

        Path resolvedRelativePath = userPath.resolve(relativePath);
        System.out.println("解析过的非绝对路径：" + resolvedRelativePath);

        // 注意：下面 path1 和 path2 的前面部分一样
        Path path1 = Paths.get("home", "fred");
        Path path2 = Paths.get("home", "fred", "harry", "input.txt");
        Path resolvedPath1 = path1.resolve(path2);
        System.out.println("第一个解析路径的结果为：" + resolvedPath1);// windows中：home\fred\home\fred\harry\input.txt
        Path resolvedPath2 = path2.resolve(path1);
        System.out.println("第二个解析的路径结果为：" + resolvedPath2);// windows中：home\fred\harry\input.txt\home\fred

        // relativize：resolve 的对立面
        // 如果：p.relativize(r) 的结果为q，则 p.relativize(q) 的结果为r
        Path relativizePath1 = path1.relativize(path2);
        System.out.println("相对化路径1：" + relativizePath1);// harry\input.txt
        Path relativizePath2 = path1.relativize(relativizePath1);
        System.out.println("相对化路径2：" + relativizePath2);// ..\..\harry\input.txt
        Path relativizePath3 = path2.relativize(path1);
        System.out.println("相对化路径3：" + relativizePath3);// ..\..
        Path relativizePath4 = path2.relativize(relativizePath1);
        System.out.println("相对化路径4：" + relativizePath4);// ..\..\..\..\harry\input.txt
        Path relativizePath5 = relativizePath1.relativize(path2);
        System.out.println("相对化路径5：" + relativizePath5);// ..\..\home\fred\harry\input.txt

        // 可以用 normalize() 方法移除所有的冗余.和..部分，但是下面这些冗余无法去除
        Path normalizePath2 = relativizePath2.normalize();
        System.out.println("去冗余后的相对化路径2：" + normalizePath2);
        Path normalizePath3 = relativizePath3.normalize();
        System.out.println("去冗余后的相对化路径3：" + normalizePath3);
        Path normalizePath4 = relativizePath4.normalize();
        System.out.println("去冗余后的相对化路径4：" + normalizePath4);
        Path normalizePath5 = relativizePath5.normalize();
        System.out.println("去冗余后的相对化路径5：" + normalizePath5);
        
        // 下面这样的冗余可以去除
        Path extraPath = Paths.get("home", "extra", "..", "harry", "work");
        System.out.println("冗余的路径：" + extraPath);
        System.out.println("去除冗余后：" + extraPath.normalize());
    }

    @Test
    public void test2() {
        Path relativePath = Paths.get("home", "harry", "work", "input.txt");
        System.out.println("相对路径：" + relativePath);

        // 下面得到的绝对路径往前面拼接的用户默认的工作目录，即使用 System.getProperty("user.dir")得到的值
        Path absolutePath = relativePath.toAbsolutePath();
        System.out.println("得到上述相对路径的绝对路径：" + absolutePath);

        Path parentPath = relativePath.getParent();
        System.out.println("获取父路径：" + parentPath);

        Path fileName = relativePath.getFileName();// 获取的是文件名：input.txt，只不过也是以Path对象表达的
        System.out.println("文件名路径：" + fileName);

        Path rootPath = relativePath.getRoot();// 获取根路径，可能为null
        System.out.println(rootPath);
        System.out.println("==============================================");

        // 如果是 \home\harry\work\output.txt
        Path relativePath2 = Paths.get("\\home", "harry", "work", "output.txt");
        System.out.println("相对路径2：" + relativePath2);

        // 在相对路径前面有分隔符的情况下，调用求其绝对路径的方法会将当前用户默认工作目录所在的盘拼接到
        // 当前相应的相对目录前面去，这是根路径也不为 null 了，不过要注意，不同系统的文件行为可能会不同，
        // 要多测试，切莫依据直觉行事。
        Path absolutePath2 = relativePath2.toAbsolutePath();
        System.out.println("绝对路径2：" + absolutePath2);

        Path rootPath2 = relativePath2.getRoot();
        System.out.println("根路径2：" + rootPath2);
    }

    // File 对象可以与 Path 对象相互转化，是绝对路径就转化为绝对路径表达的，是相对路径就转化为相对路径表达的
    @Test
    public void test3() {
        Path relativePath = Paths.get("home", "harry", "work", "input.txt");
        System.out.println("相对路径：" + relativePath);

        File file1 = relativePath.toFile();
        System.out.println("文件1：" + file1);
        System.out.println("=====================================");

        File file2 = new File("home" + File.separator + "mike" +
                File.separator + "study" + "output.dat");
        System.out.println("文件2：" + file2);
        Path path2 = file2.toPath();
        System.out.println("相对路径2：" + path2);
        System.out.println("=====================================");

        String userDir = System.getProperty("user.dir");
        File file3 = new File(userDir + File.separator + "my.txt");
        System.out.println("文件3：" + file3);
        Path absolutePath3 = file3.toPath();
        System.out.println("绝对路径3：" + absolutePath3);
    }
}
