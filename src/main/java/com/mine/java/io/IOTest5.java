package com.mine.java.io;

import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author CaoY
 * @date 2023-06-15 15:05
 * @description IO流学习 - 字符编码方式
 */
public class IOTest5 {

    @Test
    public void test1() {
        // 获取平台默认的编码方式
        Charset defaultCharset = Charset.defaultCharset();
        System.out.println("默认的编码方式：" + defaultCharset);

        System.out.println("可用的编码方式：");
        // 获取所有可用的 Charset 实例
        SortedMap<String, Charset> availableCharsets = Charset.availableCharsets();
        for (Map.Entry<String, Charset> entry : availableCharsets.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }

    @Test
    public void test2() {
        // 使用 StandardCharsets 获取编码对象
        Charset utf8 = StandardCharsets.UTF_8;
        System.out.println(utf8);

        Charset usAscii = StandardCharsets.US_ASCII;
        System.out.println(usAscii);
        System.out.println("===========================================");

        // 使用Charset.forName() 获取编码对象
        Charset charset1 = Charset.forName("UTF-8");
        System.out.println(charset1);

        Charset charset2 = Charset.forName("ASCII");
        System.out.println(charset2);
        // 由上述使用情况可知，尽量使用 StandardCharsets 的常量获取编码对象
    }
}
