package com.mine.java.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-09 15:00
 * @description 简单约简
 * 所谓 简单约简 类似于SQL中的聚合函数，是将提取流中的一些关键信息，它是流中的终结操作
 * 有时候提取的结果会放在Optional类型的对象中，存放在这里可以避免空指针带来的一些错误
 * 一些简单的约简操作如下：
 * max、min、findFirst、findAll、anyMatch、AllMatch、NoneMatch
 */
public class StreamTest4 {

    @Test
    public void test1() {
        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
//        String words = "";
        String[] splitWord = words.split(",");

        // 寻找单词排序中最大的那个单词
        Optional<String> maxOpt = Arrays.stream(splitWord).max(String::compareToIgnoreCase);
        System.out.println(maxOpt);

        // 寻找第一个以字母"d"开头的单词
        Optional<String> firstOpt = Stream.of(splitWord).filter(s -> s.startsWith("d")).findFirst();
        System.out.println(firstOpt);

        // 在并并行流中寻找任意一个以d开头的单词
        Optional<String> anyOpt = Stream.of(splitWord).parallel().filter(e -> e.startsWith("d")).findAny();
        System.out.println(anyOpt);

        // 判断流中是否存在任意的匹配
        boolean anyMatch = Stream.of(splitWord).parallel().anyMatch(e -> e.startsWith("z"));
        System.out.println(anyMatch);
    }
}
