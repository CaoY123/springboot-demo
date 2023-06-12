package com.mine.java.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-12 12:21
 * @description 并行流
 * 转化为并行流的条件：
 * （1）并行化会导致大量的开销，只有面对非常大的数据集才划算；
 * （2）只有在底层的数据源可以被有效分割为多个部分时，将流并行化才有意义；
 * （3）并行流使用的线程池可能会因诸如文件I/O或网络访问这样的操作被阻塞而饿死，只有面对海量的内存数据和运算密集处理，
 * 并行流才会工作最佳。
 */
public class StreamTest10 {

    @Test
    public void test1() {
        String[] words = {"author", "description", "stream", "data", "java", "process", "finish", "optional", "python", "springboot", "practice"};
        Stream<String> stream1 = Arrays.asList(words).parallelStream();
        Stream<String> stream2 = Stream.of(words).parallel();

        long count = stream1.filter(s -> s.length() > 6).count();
        System.out.println("Count：" + count);

        Map<Integer, Long> map1 = stream2.filter(s -> s.length() > 6)
                .collect(Collectors.groupingBy(
                        String::length,
                        Collectors.counting()
                ));
        for (Map.Entry<Integer, Long> entry : map1.entrySet()) {
            System.out.println("字符长度：" + entry.getKey() + "，单词个数：" + entry.getValue());
        }
    }

    @Test
    public void test2() throws ExecutionException, InterruptedException {
        String[] words = {"author", "description", "stream", "data", "java", "process", "finish", "optional", "python", "springboot", "practice"};
        Stream<String> stream1 = Arrays.asList(words).parallelStream();
        // 使用线程池的方式：这里看不太懂
        ForkJoinPool customPool = new ForkJoinPool();
        Set<String> set = customPool.submit(
                () -> stream1.parallel().filter(s -> s.length() > 6).collect(Collectors.toSet())
        ).get();
        System.out.println(set);
    }
}
