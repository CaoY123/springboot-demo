package com.mine.java.time;

import cn.hutool.core.date.DateUtil;
import com.mine.enums.Formatter;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author CaoYang
 * @create 2023-04-23-11:30 AM
 * @description java 8 的新加的类 —— LocalDateTime 测试类2
 */
public class LocalDateTimeTest2 {

    // 日期与时间比较操作
    @Test
    public void test1() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = time1.minusMonths(3)
                .minusDays(3)
                .minusMinutes(6)
                .plusSeconds(12);

        System.out.println(time1);
        System.out.println(time2);

        System.out.println(time1.isBefore(time2));// false
        System.out.println(time1.isAfter(time2));// true
        System.out.println(time1.isEqual(time2));// false
        System.out.println(time1.isEqual(time1));// true
    }

    // 转换操作
    @Test
    public void test2() {
        LocalDateTime nowTime = LocalDateTime.now();
        System.out.println(nowTime);

        LocalDate localDate = nowTime.toLocalDate();// 仅包含日期部分
        System.out.println(localDate);

        LocalTime localTime = nowTime.toLocalTime();// 仅包含时间部分
        System.out.println(localTime);
    }

    @Test
    public void test3() {
        LocalDateTime nowTime = LocalDateTime.now();
        System.out.println(nowTime);

        String nowTimeStr = DateUtil.format(nowTime, Formatter.YYYYMMDDHHMMSS.getPattern());
        System.out.println(nowTimeStr);
    }
}
