package com.mine.java.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author CaoY
 * @date 2023-06-09 15:29
 * @description Optional类的学习和使用
 * 当 Optional 包装的对象为空时，可以有三种处理方式：直接提供一个值；传入一个方法，计算提供一个值；抛出一个异常。
 * 慎用Optional中的 isPresent 和 get方法，因为与不包装对象的情况相比，这么用并没有什么区别。
 */
public class OptionalTest {

    // Optional包装的对象为空时的行为
    @Test
    public void test1() {
        Optional<Object> emptyOpt = Optional.empty();

        // 没有任何值时提供一个值
        Object emptyOptVal = emptyOpt.orElse("这是一个空的Optional对象");
        System.out.println(emptyOpt);
        System.out.println(emptyOptVal);

        // 没有任何值时提供一个方法计算得到一个值
        Object emptyOptVal2 = emptyOpt.orElseGet(() -> new String("计算出的值"));
        System.out.println(emptyOptVal2);

        // 没有任何值时抛出异常
//        emptyOpt.orElseThrow(() -> new RuntimeException("这里不允许提供一个空格的Optional对象"));
    }

    @Test
    public void test2() {
        Optional<String> strOpt = Optional.of(new String("SpringBoot Demo"));
        // 可选值存在时执行的操作
        strOpt.ifPresent(e -> System.out.println("如果存在，值为：" + e));

        List<String> results = new ArrayList<>();
        strOpt.ifPresent(results::add);
        System.out.println(results);
    }

    @Test
    public void test3() {
        Optional<String> strOpt = Optional.of(new String("springBoot demo"));
//        Optional<String> strOpt = Optional.empty();
        Optional<String> upperStrOpt = strOpt.map(String::toUpperCase);
        System.out.println(upperStrOpt);

        // 用filter判断条件符合与否后再进行处理，如果不符合，则会产生空的结果
        Optional<String> strOpt2 = strOpt.filter(e -> e.length() > 8).map(s -> s.substring(0, 8));
        System.out.println(strOpt2);
    }

    // ofNullable(opt)：opt为null时，生成空的Optional对象，否则生辰相应的Optional对象
    @Test
    public void test4() {
        String str1 = "name";
        String str2 = null;
        String str3 = "";
        Optional<String> opt1 = Optional.ofNullable(str1);
        Optional<String> opt2 = Optional.ofNullable(str2);
        Optional<String> opt3 = Optional.ofNullable(str3);
        System.out.println(opt1);
        System.out.println(opt2);
        System.out.println(opt3);
    }
}

