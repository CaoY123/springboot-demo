package com.mine.java.stream;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-09 10:45
 * @description 关于流的认识和学习 —— 流的创建
 */
public class StreamTest1 {

    @Test
    public void test1() {
        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
        String[] splitWord = words.split(",");
//        System.out.println(Arrays.toString(splitWord));
        List<String> wordList = Arrays.asList(splitWord);

        // 统计长度大于5的单词的个数
        long count = wordList.stream().filter(w -> w.length() > 5).count();
//        long count = wordList.parallelStream().filter(w -> w.length() < 5).count();
        System.out.println(count);
    }

    // 创建流
    @Test
    public void test2() {
        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
        String[] splitWord = words.split(",");
//        System.out.println(Arrays.toString(splitWord));
        List<String> wordList = Arrays.asList(splitWord);

        // 数组创建流，使用Stream.of()
        long count1 = Stream.of(splitWord).filter(w -> w.length() > 6).count();
        System.out.println(count1);

        // 使用Arrays.stream()
        long count2 = Arrays.stream(splitWord, 2, 4).filter(w -> "algorithm".equals(w)).count();
        System.out.println(count2);

        // 创建不包含任何元素的空流
        long count3 = Stream.empty().count();
        System.out.println(count3);

        // 无限流的创建是通过反复调用传入的方法实现的
        // 创建了一个无限流
        Stream<String> boundLessStream1 = Stream.generate(() -> "abcde");

        // 创建无限流，随机速的方式
        Stream<Double> boundLessStream2 = Stream.generate(Math::random);
//        long count4 = boundLessStream2.count();
//        System.out.println(count4);

        // 也创建了一个无限流，但是我目前不理解这些无限流的操作
        Stream<BigInteger> boundLessStream3 = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));

        // 使用Pattern.splitAsStream创建流
        Stream<String> wordsStream = Pattern.compile(",").splitAsStream(words);
        long count4 = wordsStream.count();
        System.out.println(count4);

//        System.out.println("wordsStream -> " + wordsStream);

        // 使用Files.lines()创建流，可以将文件里的每一行作为流中的一个元素
        Path path = Paths.get("src/main/resources/files/words.txt");
        try {
            long count5 = Files.lines(path).count();
            // 可以得出读取文件中的行数
            System.out.println(count5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
