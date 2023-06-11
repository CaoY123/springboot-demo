package com.mine.java.stream;

import com.mine.java.stream.entity.User;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-11 16:22
 * @description 群组和分区
 * 学习并练习了 分组操作（groupingBy、partitioningBy）以及后面的后置操作
 */
public class StreamTest7 {

    private static List<User> userList  = null;

    static {
        userList = new ArrayList<>();
        userList.add(new User(1001, "张三", "男", 22));
        userList.add(new User(1002, "李四", "女", 25));
        userList.add(new User(1003, "王五", "男", 32));
        userList.add(new User(1004, "赵六", "女", 19));
        userList.add(new User(1005, "候七", "男", 15));
    }

    @Test
    public void test1() {
        // groupingBy 更方便的可以实现分组，而不是像 StreamTest6 中那样在构建map还得逐个合并键相同的元素
        Map<String, List<Locale>> languageMap = Stream.of(Locale.getAvailableLocales())
                .filter(l -> !ObjectUtils.isEmpty(l.getCountry()))
                .collect(Collectors.groupingBy(Locale::getCountry));

        for (Map.Entry<String, List<Locale>> entry : languageMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test2() {
        // 当用返回bool值的方法来分类时，用 PartitioningBy 比 groupingBy 效率更高
        Map<Boolean, List<Locale>> languageMap = Stream.of(Locale.getAvailableLocales())
                .filter(l -> !ObjectUtils.isEmpty(l.getCountry()))
                .collect(Collectors.partitioningBy(l -> "en".equals(l.getLanguage())));

        for (Map.Entry<Boolean, List<Locale>> entry : languageMap.entrySet()) {
            System.out.println("是否为英语：" + (entry.getKey() ? "是" : "否") + "，" + entry.getValue());
        }
    }

    @Test
    public void test3() {
        // 使用一个下游收集器，希望map的值是一个 set 而非 list
        Map<String, Set<Locale>> languageMap = Stream.of(Locale.getAvailableLocales())
                .filter(l -> !ObjectUtils.isEmpty(l.getCountry()))
                .collect(Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));

        for (Map.Entry<String, Set<Locale>> entry : languageMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("***********************************************************88");

        // 使用counting() 计算每种语言的个数
        Map<String, Long> languageMap2 = Stream.of(Locale.getAvailableLocales())
                .filter(l -> !ObjectUtils.isEmpty(l.getCountry()))
                .collect(Collectors.groupingBy(Locale::getCountry, Collectors.counting()));

        for (Map.Entry<String, Long> entry : languageMap2.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test4() {
        // 下游收集器，计算男和女各自的平均年龄
        Map<String, Double> userMap = userList.stream()
                .collect(Collectors.groupingBy(
                        User::getGender,
                        Collectors.averagingDouble(User::getAge)));

        for (Map.Entry<String, Double> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("**************************************");

        // 分别寻找男、女中年龄最大的
        Map<String, Optional<User>> userMap2 = userList.stream()
                .collect(Collectors.groupingBy(
                        User::getGender,
                        Collectors.maxBy(Comparator.comparing(User::getAge))
                ));

        for (Map.Entry<String, Optional<User>> entry : userMap2.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test5() {
        // 在后置的toSet处理后，又加了一个获取set的尺寸的操作，并最终将其放到map中
        Map<String, Integer> userMap = userList.stream()
                .collect(Collectors.groupingBy(
                        User::getGender,
                        Collectors.collectingAndThen(Collectors.toSet(), Set::size)
                ));

        for (Map.Entry<String, Integer> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("*******************************");

        // 把所有国家的语言汇聚到一个集中
        Map<String, Set<String>> languageMap = Stream.of(Locale.getAvailableLocales())
                .filter(l -> !ObjectUtils.isEmpty(l.getCountry()))
                .collect(Collectors.groupingBy(
                        Locale::getCountry,
                        Collectors.mapping(Locale::getDisplayLanguage, Collectors.toSet())
                ));

        for (Map.Entry<String, Set<String>> entry : languageMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test6() {
        // 将最大最小值、数目、平均值、总和值的汇总作为后置处理
        Map<String, IntSummaryStatistics> userMap = userList.stream()
                .collect(Collectors.groupingBy(
                        User::getGender,
                        Collectors.summarizingInt(User::getAge)
                ));

        for (Map.Entry<String, IntSummaryStatistics> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
        System.out.println("*************************************");
    }
}
