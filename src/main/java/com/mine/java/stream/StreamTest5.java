package com.mine.java.stream;

import com.mine.java.stream.entity.User;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CaoY
 * @date 2023-06-10 11:17
 * @description 流的结果的收集
 * 下面练习了一些简单的结果收集操作，包括：直接用forEach遍历，将结果放到数组、不同的集合中，
 * 将结果转化为一个字符串并在其中加入一些间隔符，汇总出我们关心的属性的元素个数、最大最小值、平均值和总和
 * 以及得到最终结果的一个迭代器，以用于后续的遍历处理。
 *
 * 对 stream 中 collect 的理解：
 * 使用给定的收集器来收集最后的结果，这些收集器可以是集合、数组、一些汇总（定义好的数据结构）、以及经过加工的字符串
 *
 */
public class StreamTest5 {

    @Test
    public void test1() {
        List<String> list = Arrays.asList("name", "SpringBoot", "you", "might", "time", "support", "minute", "year", "author", "description", "stream", "test");

        // forEach 遍历结果集
        list.stream().filter(s -> s.length() > 5).forEach(s -> System.out.print(s + ", "));
        System.out.println();

        // 并行流的forEach不一定是按单词顺序来的
        list.parallelStream().filter(s -> s.length() > 5).forEach(s -> System.out.print(s + ", "));
        System.out.println();

        // 使用forEachOrdered来按顺序遍历并行流，但是这显然会使得执行速度降低，进而并行流就失去了意义，所以应该注意它的应用场景
        list.parallelStream().filter(s -> s.length() > 5).forEachOrdered(s -> System.out.print(s + ", "));
        System.out.println();

        // 将结果收集到一个数组中
        String[] words = list.stream().filter(s -> s.length() > 5).toArray(String[]::new);
        System.out.println("Array：" + Arrays.toString(words));

        // 将结果收集到另一个目标（集合）中
        // 存到list中
        List<String> wordList = list.stream().filter(s -> s.length() > 5).collect(Collectors.toList());
        System.out.println("List：" + wordList);

        // 存到set中
        Set<String> wordSet = list.stream().filter(s -> s.length() > 5).collect(Collectors.toSet());
        System.out.println("Set：" + wordSet);

        // 存到treeSet中
        TreeSet<String> wordTreeSet = list.stream().filter(s -> s.length() > 5).collect(Collectors.toCollection(TreeSet::new));
        System.out.println("wordTreeSet：" + wordTreeSet);

        // 在元素之间添加分隔符
        String wordStrWithSplit = list.stream().filter(s -> s.length() > 5).collect(Collectors.joining(";"));
        System.out.println("Word string with split：" + wordStrWithSplit);

        // 使用joining()直接连接所有字符
        String wordStr = list.stream().filter(s -> s.length() > 5).collect(Collectors.joining());
        System.out.println("Word string：" + wordStr);
    }

    @Test
    public void test2() {
        // String 中含有多个类型元素
        List list = new ArrayList();
        list.add("name");
        list.add(1);
        list.add(new BigInteger("213456789"));
        list.add(new User(10001, "张三", "男", 28));

        // 对于集合中含有多个元素类型不同的情况，可以先将其统一映射为String类型，再进行连接
        String listStr = (String) list.stream().map(Object::toString).collect(Collectors.joining(";"));
        System.out.println(listStr);
    }

    @Test
    public void test3() {
        List<Integer> list = Arrays.asList(12, 34, 33, 21, 9, 56, 72, 83, 19, 61);
        // 下面的汇总能求出汇总的元素个数、最大值、最小值、平均值和总量
        IntSummaryStatistics summary = list.stream().filter(n -> n > 40).collect(Collectors.summarizingInt(n -> n));
        System.out.println("Summary：" + summary);
        Integer sum = list.stream().filter(n -> n > 40).collect(Collectors.summingInt(n -> n));
        System.out.println("Sum：" + sum);

        System.out.println("************************迭代器打印的情况************************");
        // 使用迭代器
        Iterator<Integer> iter = list.stream().filter(n -> n > 40).iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
