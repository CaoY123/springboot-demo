package com.mine.java.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author CaoY
 * @date 2023-06-11 20:50
 * @description 约简操作
 * reduce 方法是一种用于从流中计算某个值的通用机制，其最简单的形式是接受一个二元函数，并从前两个函数开始持续应用它。
 * 主要介绍了reduce的用法，reduce是一种用于从流中计算某个值的通用机制，但是要注意线程安全问题，它实现的约简效果是：
 * v0 op v1 op v2 ......（op为操作符）
 */
public class StreamTest8 {

    @Test
    public void test1() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        // 用 reduce 方法来求所有元素的和
//        Optional<Integer> opt1 = list.stream().reduce((a, b) -> a + b);
        // 与上面的效果一样
        Optional<Integer> opt1 = list.stream().reduce(Integer::sum);
        System.out.println(opt1);

        // 求最大值
        Optional<Integer> opt2 = list.stream().reduce(Integer::max);
        System.out.println(opt2);

        // 求累乘
        Optional<Integer> opt3 = list.stream().reduce((a, b) -> a * b);
        System.out.println(opt3);
    }

    @Test
    public void test2() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // 可以传入一个初始值，在这个初始值上进行操作，那么这时就不需要Optional对象了
        Integer opt1 = list.stream().reduce(1, Integer::sum);
        System.out.println(opt1);

        Integer opt2 = list.stream().reduce(0, (a, b) -> a * b);
        System.out.println(opt2);
    }

    /**
     * 场景：现在有一个对象流，想要对它们的某些属性求和。
     * 如：求下面字符串流的字符的总个数，而对于reduce方法，传入的函数为（T, T） -> T，即引元和结果类型相同；
     * 但是在该场景下，传入的是两种类型，即：流的元素是String类型，而累计的结果是整数，下面的传入三个参数的reduce的情况
     * 可以解决上述问题。需要提供一个累积器，即下面第二个参数：(total, word) -> total + word.length()，但是当
     * 计算并行化时，会有多个这种类型的计算，需要将它们的结果合并。因此提供第三个参数进行各个total的合并：
     * (total1, total2) -> total1 + total2。这种特征也使得要想使用reduce，必须保证线程安全
     */
    @Test
    public void test3() {
        List<String> list = Arrays.asList("name", "string", "high", "code", "process", "finish", "stream", "warning");
        Integer res1 = list.stream().reduce(
                0,
                (total, word) -> total + word.length(),
                (total1, total2) -> total1 + total2
        );
        System.out.println(res1);
    }
}
