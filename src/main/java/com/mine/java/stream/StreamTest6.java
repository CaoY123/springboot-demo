package com.mine.java.stream;

import com.mine.java.stream.entity.User;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-11 11:15
 * @description 将流的结果收集到映射表中
 * 下面介绍了将流的结果转换为 Map的基本操作
 */
public class StreamTest6 {

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
        // 构造id 到 User对象的集合
        Map<Integer, User> userMap = userList.stream()
                .filter(u -> "男".equals(u.getGender()))
                .collect(Collectors.toMap(User::getId, Function.identity()));

        System.out.println("****************************Map 集合****************************");
        for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test2() {
        // 如果在集合中多个元素具有相同的键，那么就会造成冲突，收集器会抛出一个IllegalStateException异常

        // 下面这个语句构建一个存储所有可用locale语言的Map，其中：每种语言在默认locale中的名字（如“German”）
        // 为键，而其本地化的名字（如：“Deutsch”）为值。但是这样会造成冲突，如瑞士和德国都使用德语，主要看一下
        // 这个抛出的异常。
//        Stream.of(Locale.getAvailableLocales())
//                .collect(Collectors.toMap(
//                        Locale::getDisplayLanguage,
//                        loc -> loc.getDisplayLanguage(loc)));

        // 为了解决上述问题，可以指定第三个函数引元，即发生冲突是的合并策略。这里的合并策略为：
        // 当新旧键冲突的时候，保留旧键的值
        Map<String, String> LanguageMap = Stream.of(Locale.getAvailableLocales())
                .collect(Collectors.toMap(
                        Locale::getDisplayLanguage,
                        loc -> loc.getDisplayLanguage(loc),
                        (existVal, newVal) -> existVal));

        for (Map.Entry<String, String> entry : LanguageMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test3() {
        // 了解给定国家的所有语言，用一个Map来存储最后的结果，其中键为国家的名字，值为由这个国家使用语言构成的Set集合
        // 而在这里对于键冲突采用了合并Set集的策略
        Map<String, Set<String>> languageMap = Stream.of(Locale.getAvailableLocales())
                .filter(l -> !ObjectUtils.isEmpty(l.getDisplayCountry()))
                .collect(Collectors.toMap(
                        Locale::getDisplayCountry,
                        l -> Collections.singleton(l.getDisplayLanguage()),
                        (a, b) -> {
                            Set<String> union = new HashSet<>(a);
                            union.addAll(b);
                            return union;
                        }
                ));

        for (Map.Entry<String, Set<String>>entry : languageMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test4() {
        // 构建一个TreeMap，传入了四个参数，其中指定合并函数为抛出异常（即有键相同的情况就抛异常）
        Map<Integer, User> userMap = userList.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        Function.identity(),
                        (existingVal, newVal) -> {
                            throw new IllegalStateException();
                        },
                        TreeMap::new
                ));

        for (Map.Entry<Integer, User> entry : userMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
