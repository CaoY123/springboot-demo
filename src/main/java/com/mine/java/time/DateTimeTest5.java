package com.mine.java.time;

import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * @author CaoY
 * @date 2023-07-06 2:38
 * @description 日期和时间 API - 格式化和解析
 *
 * 下面的代码练习了将日期和时间按我们想要的格式输出以及解析。
 * 此外，现有的在 Java8 中新增的日期时间操作类与过去的日期时间类之间也可以相互转换，在这里不作练习，
 * 遇到查阅相应 API 即可。
 */
public class DateTimeTest5 {

    @Test
    public void test1() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("默认：" + now);
        System.out.println("====================");
        // 标准的格式器，主要是给机器可读用的
        String format1 = DateTimeFormatter.ISO_DATE_TIME.format(now);
        System.out.println("format1: " + format1);
        System.out.println("====================");
        // 使用 locale 供人类阅读
        // 使用默认的 locale
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        String format2 = formatter.format(now);
        System.out.println("format2: " + format2);
        System.out.println("====================");
        // 英语格式的时间表达，需要操作时区时间
        ZonedDateTime zonedNow = now.atZone(ZoneId.systemDefault());
        String format3 = formatter.withLocale(Locale.ENGLISH).format(zonedNow);
        System.out.println("format3: " + format3);
    }

    // 使用 Locale 打印不同地方的星期信息
    @Test
    public void test2() {
        for (DayOfWeek w : DayOfWeek.values()) {
            System.out.print(w.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
            System.out.println(", " + w.getDisplayName(TextStyle.SHORT, Locale.CHINA));
        }
    }

    // 定制自己的日期时间输出格式
    @Test
    public void test3() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss");
        String format1 = formatter.format(now);
        System.out.println("format1: " + format1);
        // 解析字符串中的日期时间，默认使用标准的格式解析器
        LocalDate parsedDate = LocalDate.parse("2000-09-21");
        System.out.println("parsedDate: " + parsedDate);

        // 指定解析的模式
        LocalDateTime parsedDateTime = LocalDateTime.parse(format1, DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm:ss"));
        System.out.println("上述 format1 解析后的日期时间为：" + parsedDateTime);
    }
}
