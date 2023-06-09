package com.mine.java.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-09 13:26
 * @description 抽取子流 和 组合流
 * 介绍了limit、skip以及concat的基本用法：
 * limit(n)：选取流中前面n个元素；
 * skip(n)：排除流中前n个元素；
 * concat(n)：连接两个流
 *
 * 还介绍了其他的转换流：
 * distinct：提取流中不重复的元素；
 * sorted：对流中的元素进行排序；
 * peek：在获取每个元素时，执行传入的动作
 */
public class StreamTest3 {

    @Test
    public void test1() {
        // 产生一个包含100个随机数的流，用limit限定流的规模，其中 limit(n)取的是前n个元素
        Stream.generate(Math::random).limit(100).forEach(num -> System.out.print(num + ", "));

        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
        String[] splitWord = words.split(",");
        System.out.println();

        // 忽略前3个单词而保留剩下的单词
        Stream.of(splitWord).skip(3).forEach(word -> System.out.print(word + ", "));
        System.out.println();

        String str1 = "hello";
        String str2 = "world";
        // 合并两个流
        Stream.concat(codePoints(str1), codePoints(str2)).forEach(ch -> System.out.print(ch + ", "));
    }

    public static Stream<String> codePoints(String s) {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int j = s.offsetByCodePoints(i, 1);
            list.add(s.substring(i, j));
            i = j;
        }
        return list.stream();
    }

    @Test
    public void test2() {
        // distinct 去重操作
        Stream.of("name", "name", "merely", "name", "merely", "sort").distinct().forEach(s -> System.out.print(s + ", "));

        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
        String[] splitWord = words.split(",");
        System.out.println();

        // 按单词长度从大到小排序
        Stream.of(splitWord).sorted(Comparator.comparing(String::length).reversed()).forEach(s -> System.out.print(s + ", "));

        Object[] objects = Stream.of(splitWord).peek(e -> System.out.println("Fetch: " + e)).toArray();
        System.out.println(Arrays.toString(objects));
    }
}
