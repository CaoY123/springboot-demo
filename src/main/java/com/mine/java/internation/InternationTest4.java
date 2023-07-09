package com.mine.java.internation;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * @author CaoY
 * @date 2023-07-09 14:40
 * @description 国际化的学习 - 日期和时间
 *
 * 1. 当格式化日期和时间时，需要考虑 4 个与 locale 相关的问题：
 *  1）月份和星期应该用本地语言来表示；
 *  2）年、月、日的顺序要符合本地习惯；
 *  3）公历可能不是本地首选的日期表示方法；
 *  4）必须要考虑本地的时区。
 *
 * 2. 使用 DateTimeFormatter 解决
 *
 */
public class InternationTest4 {

    // 下面将日期时间转换成不同国家的形式
    // 注意：一些日期和时间的格式在一些国家的日期和时间表示中不存在，因此会报错，要及时进行验证，确保转换的正确性。
    @Test
    public void test1() {
        FormatStyle fullStyle = FormatStyle.FULL;
        FormatStyle shortStyle = FormatStyle.SHORT;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(fullStyle)
                .withLocale(Locale.US);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(shortStyle)
                .withLocale(Locale.US);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.US);

        LocalDateTime nowDateTime = LocalDateTime.now();
        String formatDate = dateFormatter.format(nowDateTime);
        System.out.println(formatDate);

        String formatTime = timeFormatter.format(nowDateTime);
        System.out.println(formatTime);

        String formatDateTime = dateTimeFormatter.format(nowDateTime);
        System.out.println(formatDateTime);
        System.out.println("===================================");
        // 使用 LocalDate、LocalTime、LocalDateTime的 parse 方法并指定相应的 formatter
        // 反向解析输入的日期和时间字符串
        LocalDate parsedDate = LocalDate.parse(formatDate, dateFormatter);
        System.out.println("解析 【 " + formatDate + " 】后得到【 " + parsedDate + " 】");
    }
}
