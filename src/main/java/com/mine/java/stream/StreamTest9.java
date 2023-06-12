package com.mine.java.stream;

import org.junit.Test;

import java.util.IntSummaryStatistics;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-12 10:50
 * @description 基本类型流
 * 将数字包装到包装对象中是比较低效的，因此直接处理基本类型更好，这就是基本类型流的意义。
 * 流库中的IntStream、LongStream、DoubleStream来存储基本类型值。
 * 其中int、char、byte、short、boolean存储与IntStream；
 * long存储与LongStream中；
 * float、double存储与DoubleStream中
 */
public class StreamTest9 {

    @Test
    public void test1() {
        IntSummaryStatistics summary = IntStream.of(1, 2, 3, 4, 5, 6, 7).summaryStatistics();
        System.out.println(summary);

        // 生成0 - 9
        IntStream.range(0, 10).forEach(e -> System.out.print(e + ", "));
        System.out.println();

        // 生成0 - 10
        IntStream.rangeClosed(0, 10).forEach(e -> System.out.print(e + ", "));
        System.out.println();

        // CharSequence 接口拥有 codePoints() 和 chars()，可以生成由字符的 Unicode 码
        // 或由UTF-16编码机制的码元构成的 IntStream
        String sentence = "\uD835\uDD46 is the set of octonions.";
        sentence.codePoints().forEach(e -> System.out.print(e + ", "));
        System.out.println();
        sentence.chars().forEach(e -> System.out.print(e + ", "));
    }
    
    @Test
    public void test2() {
        // 将对象流转换为基本类型流：使用mapToInt/mapTODouble/mapToLong
        Stream.of("name", "system", "test", "time", "transport", "listen", "util", "stream", "springboot")
                .mapToInt(String::length)
                .forEach(e -> System.out.print(e + ", "));
        System.out.println();

        // 将基本类型流使用对象流，使用 boxed()
        Stream<Integer> boxedStream = IntStream.rangeClosed(0, 12).boxed();
        boxedStream.forEach(e -> System.out.print(e +  ", "));
    }

    @Test
    public void test3() {
        // Random 的 ints、longs、doubles方法可以生成基本类型流
        Random random = new Random(System.currentTimeMillis());// 设置种子为当前系统时间
        IntStream intStream = random.ints(10, 0, 30);
        intStream.forEach(e -> System.out.print(e + ", "));
    }

    @Test
    public void test4() {
        // 调用得到可选值的方法
        OptionalDouble averageNum = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .mapToInt(a -> a)
                .average();
        System.out.println("Average：" + averageNum);
    }
}
