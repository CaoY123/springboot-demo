package com.mine.java.string;

import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-04-08 14:42
 * @description 对于String类型的测试
 */
public class StringTest {

    @Test
    public void test1() {
        String s1 = new String("aaa");
        String s2 = new String("aaa");
        System.out.println(s1 == s2);           // false
        String s3 = s1.intern();
        System.out.println(s2.intern() == s3);  // true
    }

    @Test
    public void test2() {

        String s1 = "bbb";
        String s2 = "bbb";
        System.out.println(s1 == s2);
    }

}
