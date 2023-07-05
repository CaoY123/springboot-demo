package com.mine.java.time;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author CaoY
 * @date 2023-07-05 16:22
 * @description 日期和时间 API - 时间线
 *
 * 下面的联系主要是 Instant 以及 Duration 相关的。两个 Instant 之间的时长是 Duration。
 */
public class DateTimeTest1 {

    @Test
    public void test1() throws InterruptedException {
        // 有种岁月感
        System.out.println("当前时间：" + Instant.now());
        System.out.println("Instant 最小时间：" + Instant.MIN);
        System.out.println("Instant 最大时间：" + Instant.MAX);
        System.out.println("=============================");

        Random random = new Random(System.currentTimeMillis());
        Instant start = Instant.now();
        Thread.sleep(random.nextInt(2000));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("时间间隔：" + duration.toMillis() + "毫秒");
        System.out.println("=============================");

        Instant now = Instant.now();
        Instant day1 = now.plus(12L, ChronoUnit.DAYS);
        System.out.println("day1：" + day1);
    }

    /**
     * 分别测试下面 runAlgorithm 和 runAlgorithm2 的执行时间，这两个算法都是排序算法
     */
    @Test
    public void test2() {
        Instant start = Instant.now();
        runAlgorithm();
        Instant end = Instant.now();
        Duration timeElapsed = Duration.between(start, end);
        System.out.println("runAlgorithm的执行时间：" + timeElapsed.toMillis() + "毫秒");

        Instant start2 = Instant.now();
        runAlgorithm2();
        Instant end2 = Instant.now();
        Duration timeElapsed2 = Duration.between(start2, end2);
        System.out.println("runAlgorithm2的执行时间：" + timeElapsed2.toMillis() + "毫秒");

        // 可以用来衡量两个算法执行速度相差的数量级
        boolean overTenTimesFaster = timeElapsed.multipliedBy(10).minus(timeElapsed2).isNegative();
        System.out.println("runAlgorithm 的速度比 runAlgorithm2 的速度快10倍以上吗：" +
                (overTenTimesFaster ? "是" : "否"));
    }

    public static void runAlgorithm() {
        int size = 10;
        List<Integer> list = new Random().ints().map(i -> i % 100).limit(size)
                .boxed().collect(Collectors.toList());
        Collections.sort(list);
        System.out.println(list);
    }

    // 如果列表不是按升序排列，则会对列表进行乱序操作，并输出乱序后的列表。然后循环继续，直到列表按升序排列为止。
    // 牛逼，猴子排序，打乱到我满意为止
    public static void runAlgorithm2() {
        int size = 10;
        List<Integer> list = new Random().ints().map(i -> i % 100).limit(size)
                .boxed().collect(Collectors.toList());
        while (!IntStream.range(1, list.size())
                .allMatch(i -> list.get(i - 1).compareTo(list.get(i)) <= 0)) {
            Collections.shuffle(list);
        }
        System.out.println(list);
    }
}
