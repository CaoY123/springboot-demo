package com.mine.exception;

import org.junit.Test;

import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-04-08 12:58
 * @description 异常执行的顺序测试
 * 注意try-catch-finally的语句块的执行顺序
 */
public class ExceptionTest {

    @Test
    public void test1() {

        int num  = func1();
        System.out.println("num = " + num);
    }

    @Test
    public void test2() {
        try (Scanner scanner = new Scanner("aaa.txt")) {
            // do something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            int num = 12 / 0;
        } catch (NumberFormatException numberFormatException) {

        } catch (IllegalArgumentException exception) {

        }
    }

    @Test
    public void test4() {

        int num = func2();
        System.out.println("num = " + num);
    }

    public int func1() {
        try {
            System.out.println("被监控的代码...");
            // 有异常的情况
//            int num = 12 / 0;
            // 没有异常的情况
            int num = 12 / 3;
        } catch (Exception e) {
            System.out.println("异常处理语句...");
            return 2;
        } finally {
            System.out.println("finally语句块...");
        }
        System.out.println("finally语句块之后的语句...");
        return 0;
    }

    // 执行顺序注意：先执行return的部分，在函数即将返回前执行finally的部分
    public int func2() {
        try {
            System.out.println("被监控的代码...");
            return fun2Func();
        } catch (Exception e) {
            System.out.println("异常处理语句...");
            return 2;
        } finally {
            System.out.println("finally语句块...");
        }
    }

    public int fun2Func() {
        System.out.println("调用了func2Func()...");
        return 3;
    }

}
