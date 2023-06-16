package com.mine.java.io;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-15 10:20
 * @description IO流学习 - 读取文本输入
 * 下面的练习展示了使用 Scanner、Files、InputStreamReader 读取文本文件的情况。
 * 与 Scanner 不同的是，Files、InputStreamReader 不能直接读入数字，而 Scanner 却可以
 */
public class IOTest3 {

    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "words.txt";
        // 使用 Scanner 读入
        Scanner scanner = new Scanner(new File(path), StandardCharsets.UTF_8.name());
        List<String> wordList = new ArrayList<>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            wordList.add(word);
        }
        System.out.println("一共有 " + wordList.size() + " 个单词");
        System.out.println(wordList);


        String path2 = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "Of_Studies.txt";
        Path filePath2 = Paths.get(path2);
        // 按行读入文件
        List<String> passages = Files.readAllLines(filePath2, StandardCharsets.UTF_8);
        System.out.println("文章的段落数：" + passages.size() + "，文章的内容如下：");
        passages.forEach(System.out::println);
        System.out.println("===============================================");

        // 按行读入文件并将其惰性处理为一个Stream<String>流
        Stream<String> linesStream = Files.lines(filePath2, StandardCharsets.UTF_8);
        List<String> passageList = linesStream.collect(Collectors.toList());
        System.out.println("文章一共有 " + passageList.size() + " 段，文章内容如下：");
        passageList.forEach(System.out::println);
    }

    @Test
    public void test2() throws FileNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "Of_Studies.txt";
        // 这里使用句点进行分割句子的时候，需要进行转义
        Scanner scanner = new Scanner(new File(path), StandardCharsets.UTF_8.name())
                .useDelimiter("\\.");
        List<String> sentences = new ArrayList<>();
        while (scanner.hasNext()) {
            String sentence = scanner.next();
            sentences.add(sentence);
        }
        System.out.println("文章一共有 " + sentences.size() + " 句话，内容为：");
        for (int i = 0; i < sentences.size(); i++) {
            System.out.println("第" + (i + 1) + "句：" + sentences.get(i));
        }
        System.out.println("===============================================");
    }

    @Test
    public void test3() throws FileNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "Of_Studies.txt";
        // Java 的早期版本读取文本文件，使用 InputStreamReader 读取文件，InputStreamReader 包装了一个
        // InputStream(字节输入流)对象，并将其转换为一个Reader（字符输入流）对象，以便进行文本处理的操作。
        // 而且，搭配缓冲区（即将 InputStreamReader 对象包装到 BufferedReader）中使用效率更高，但是要注意
        // 缓冲区的内容是否刷新到目标文件中
        FileInputStream fis = new FileInputStream(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8.name()))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "Of_Studies.txt";
        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        // 使用 lines() 产生一个 Stream 流
        Stream<String> passageStream = br.lines();
        List<String> lines = passageStream.collect(Collectors.toList());
        lines.forEach(System.out::println);

        // 这里关闭要按照顺序将上面创建的流都关闭掉，其实还要判断 fis、isr、br 是否为 null，但是
        // 为了代码清晰，就省略这个步骤了。其实这种情况，最好使用 try-with-resources 语句块包起来
        // 这些流可以被自动关闭，如果发生什么异常，也会捕获以进行处理
        fis.close();
        isr.close();
        br.close();
    }
}
