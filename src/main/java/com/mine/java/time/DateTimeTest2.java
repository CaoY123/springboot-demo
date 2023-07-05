package com.mine.java.time;

import org.junit.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * @author CaoY
 * @date 2023-07-05 21:03
 * @description 日期和时间 API - 本地时间
 *
 * 1. Java API 中有两种人类时间：本地日期/时间、时区时间。本地日期/时间包含日期和当天的时间，但是与时区信息无关。
 * 2. 很多情况下，时区时间可能会成为一种障碍，所以 API 的设计者们不推荐我们使用时区时间，除非确实想要表示绝对时间的实例。
 * 3. 两个本地时间的间隔是 Period。
 */
public class DateTimeTest2 {

    @Test
    public void test1() {
        LocalDate now = LocalDate.now();
        System.out.println("now: " + now);
        LocalDate date1 = LocalDate.of(1998, 3, 25);
        System.out.println("date1: " + date1);
        LocalDate date2 = LocalDate.of(2014, 1, 1).plusDays(255);
        System.out.println("date2: " + date2);
        Period period1 = date1.until(date2);
        System.out.println("date1 与 date2 间隔：" + period1.getYears() + "年");
        // 只会比较日期上的数值，因此不是很有用，可以看成是历史上的“今天”
        System.out.println("date1 与 date2 间隔：" + period1.getDays()+ "天");
        long period2 = date1.until(date2, ChronoUnit.DAYS);
        System.out.println("data1 与 date2 实际相差的天数：" + period2 + "天");
        System.out.println("============================");
        // 创建一个并不存在的日期 2月31日
        LocalDate date3 = LocalDate.of(2000, 1, 31).plusMonths(1);
        // 实际上为 2月29日
        System.out.println("date3: " + date3);
    }

    @Test
    public void test2() {
        // MonthDay：没有年份，描述月日的
        MonthDay monthDay1 = MonthDay.of(2, 15);
        System.out.println("monthDay1: " + monthDay1);
        // 创建不可能存在的日期会报错
//        MonthDay monthDay2 = MonthDay.of(2, 30);
//        System.out.println("monthDay2: " + monthDay2);

        // YearMonth：描述月日
        YearMonth yearMonth1 = YearMonth.of(2009, 8);
        System.out.println("yearMonth1: " + yearMonth1);
        // 下面的语句也会报错
//        YearMonth yearMonth2 = YearMonth.of(2009, 13);
//        System.out.println("yearMonth2: " + yearMonth2);

        // Year：描述年
        Year year1 = Year.of(2007);
        System.out.println("year1: " + year1);
    }

    @Test
    public void test3() {
        LocalDate now = LocalDate.now();
        System.out.println("now: " + now);
        int dayOfMonth = now.getDayOfMonth();
        System.out.println("dayOfMonth: " + dayOfMonth);
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        System.out.println("dayOfWeek: " + dayOfWeek);
        int dayOfYear = now.getDayOfYear();
        System.out.println("dayOfYear: " + dayOfYear);
        LocalDate withYear = now.withYear(2008);// 改变日期中的年份，同理也可以改变月和日
        System.out.println("withYear: " + withYear);
        int monthValue = now.getMonthValue();
        System.out.println("monthValue: " + monthValue);
        System.out.println(withYear + "是否在" + now + "之后：" + (withYear.isAfter(now) ? "是" : "否"));
        System.out.println(now + "是闰年吗：" + (now.isLeapYear() ? "是" : "否"));
    }
}
