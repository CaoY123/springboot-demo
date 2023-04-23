package com.mine.java.time;

import org.junit.Test;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * @author CaoYang
 * @create 2023-04-23-10:45 AM
 * @description java 8 的新加的类 —— LocalDateTime 测试类1
 */
public class LocalDateTimeTest1 {

    // 创建实例
    @Test
    public void test1() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

//        LocalDateTime time = LocalDateTime.of(2002, 19, 32, 12, 20); // 错误：注意构造的日期或时间的范围，这么设计是合理的
        LocalDateTime time = LocalDateTime.of(2002, 04, 12, 3, 50);// 不带秒数
        System.out.println(time);

        LocalDateTime time2 = LocalDateTime.of(2005, 7, 30, 15, 23, 5);// 不带毫秒数
        System.out.println(time2);

        LocalDateTime time3 = LocalDateTime.of(2009, 6, 12, 9, 10, 23, 17);// 最后是9位纳秒数
        System.out.println(time3);
    }

    // 获取日期和时间
    @Test
    public void test2() {
        LocalDateTime nowTime = LocalDateTime.now();
        int year = nowTime.getYear();
        System.out.println("年份：" + year);

        Month month = nowTime.getMonth(); // 年份
        System.out.println("月份：" + month); // 英文月份

        int dayOfMonth = nowTime.getDayOfMonth(); // 月份第多少天
        System.out.println("当月第 " + dayOfMonth + " 天");

        int hour = nowTime.getHour();
        System.out.println("小数数：" + hour);

        int minute = nowTime.getMinute();
        System.out.println("分钟数：" + minute);

        int second = nowTime.getSecond();
        System.out.println("秒数：" + second);

        int nano = nowTime.getNano();
        System.out.println("纳秒数：" + nano);
    }

    // 对 Month 类进行测试 (部分)
    @Test
    public void test3() {
        // 获取月份实例
        Month month = LocalDateTime.now().getMonth();
        System.out.println(month);

        int monthValue = month.getValue();
        System.out.println("月份整数值：" + monthValue);

        // 根据整数值构造月份
        Month month2 = Month.of(11);
        System.out.println(month2);

        // 获取上一个月
        Month month3 = month.minus(1);
        System.out.println(month3);

        // 获取下一个月
        Month month4 = month.plus(1);
        System.out.println(month4);

        // 本地化月份全名
        String localeFullName = month.getDisplayName(TextStyle.FULL, Locale.SIMPLIFIED_CHINESE);
        System.out.println("本地化月份全名：" + localeFullName);

        // 本地化月份缩写
        String localeShortName = month.getDisplayName(TextStyle.SHORT, Locale.SIMPLIFIED_CHINESE);
        System.out.println("本地化月份缩写：" + localeShortName);

        // 英式化月份全名
        String englishFullName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        System.out.println("英式月份全名：" + englishFullName);

        // 英式化月份缩写
        String englishShortName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        System.out.println("英式化月份缩写：" + englishShortName);
    }

    // 操作日期和时间 - plus
    @Test
    public void test4() {
        LocalDateTime nowTime = LocalDateTime.now();
        System.out.println(nowTime);

        LocalDateTime time2 = nowTime.plusDays(1); // 加 1 天
        System.out.println(time2);

        LocalDateTime time3 = nowTime.plusDays(-1); // 减 1 天
        System.out.println(time3);

        LocalDateTime time4 = nowTime.plusMinutes(-1440); // 减 1440 分钟， 实际上相当与 减 1 天
        System.out.println(time4);

        LocalDateTime time5 = nowTime.plusSeconds(3); // 加 3 秒
        System.out.println(time5);

        LocalDateTime time6 = nowTime.plusNanos(1000000);// 加 1000000 纳秒，在加够整毫秒数的情况下最后是3位毫秒数
        System.out.println(time6);

        LocalDateTime time7 = nowTime.plusNanos(3);// 加 3 纳秒，在不加够整毫秒数的情况下最后是9位纳秒数
        System.out.println(time7);
    }

    // 操作日期和时间 - minus
    @Test
    public void test5() {
        LocalDateTime nowTime = LocalDateTime.now();
        System.out.println(nowTime);

        LocalDateTime time1 = nowTime.minusYears(1);// 减 1 年
        System.out.println(time1);

        LocalDateTime time2 = nowTime.minusMonths(-2);// 加 2 月
        System.out.println(time2);

        LocalDateTime time3 = nowTime.minusDays(3);// 减 3 天
        System.out.println(time3);

        LocalDateTime time4 = nowTime.minusHours(4);
        System.out.println(time4);// 减 4 小时

        LocalDateTime time5 = nowTime.minusMinutes(6);// 减 6 分钟
        System.out.println(time5);

        LocalDateTime time6 = nowTime.minusSeconds(9);// 减 9 秒钟
        System.out.println(time6);

        LocalDateTime time7 = nowTime.minusNanos(7);// 减 7 纳秒
        System.out.println(time7);// 最后是 9 位纳秒数

        LocalDateTime time8 = nowTime.minusNanos(1000000);// 减 1000000 纳秒（相当于1毫秒）
        System.out.println(time8);
    }
}
