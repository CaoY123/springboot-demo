package com.mine.utils;

import org.springframework.util.StopWatch;

/**
 * @author CaoYang
 * @create 2023-04-07-10:16 AM
 * @description StopWatch示例
 * 参考链接：https://wenjie.store/archives/springboot-about-stopwatch-1#%E5%89%8D%E8%A8%80
 */
public class StopWatchTest {

    public static void main(String[] args) throws Exception {
        StopWatch myWatch = new StopWatch("myWatch");
        doSomething(2000L, myWatch);
        doSomething(3000L, myWatch);
        doSomething(1000L, myWatch);

        System.out.println("总列表输出：");

        System.out.println(myWatch.prettyPrint());
    }

    private static void doSomething(long millis, StopWatch myWatch) throws Exception {
        myWatch.start("task" + (myWatch.getTaskCount()+1));
        Thread.sleep(millis);
        myWatch.stop();
        System.out.println("StopWatch 'myWatch': LastTask time (millis) = " + myWatch.getLastTaskTimeMillis());
        System.out.println(myWatch.shortSummary());
    }
}
