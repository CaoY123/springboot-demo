package com.mine.utils;


import org.springframework.util.StopWatch;

/**
 * @author CaoYang
 * @create 2023-04-07-10:26 AM
 * @description
 */
public class StopWatchTest2 {
    public static void main(String[] args) throws InterruptedException {
        StopWatch sw = new StopWatch();
        sw.start("读取文件");
        Thread.sleep(1000);
        sw.stop();
        sw.start("文件删除");
        Thread.sleep(100);
        sw.stop();
        sw.start("文件拷贝");
        Thread.sleep(10);
        sw.stop();
        System.out.println(sw.prettyPrint());

        long stime =System.currentTimeMillis();
        Thread.sleep(1000);
        long etime =System.currentTimeMillis();
        System.out.println("执行时间:"+(etime-stime));
    }
}
