package com.mine.java.internation;

import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author CaoY
 * @date 2023-07-08 22:15
 * @description 国际化的学习 - 数字格式
 */
public class InternationTest2 {

    @Test
    public void test1() throws ParseException {
        // 定义一个 Locale 对象
        Locale usLocale = Locale.US;
        // 使用工厂方法获得相应 locale 的货币格式
        NumberFormat currFmt = NumberFormat.getCurrencyInstance(usLocale);
        double amt = 123456.78;
        // 得到该量的格式化后的字符串
        String res = currFmt.format(amt);
        System.out.println(res);

        // 将 res 反向解析
        // 注：Number 类型的数据不能直接转换为基本类型，需要调用诸如 doubleValue() 等方法
        double amount = currFmt.parse(res.trim()).doubleValue();
        System.out.println("按相应的格式解析货币字符串得：" + amount);
    }
}
