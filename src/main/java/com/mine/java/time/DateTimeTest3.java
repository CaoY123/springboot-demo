package com.mine.java.time;

import org.junit.Test;

import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * @author CaoY
 * @date 2023-07-05 22:26
 * @description 日期和时间 API - 日期调整器 和 本地时间
 */
public class DateTimeTest3 {

    // 日期调整器
    @Test
    public void test1() {
        // 返回下一个星期二
        LocalDate nextTuesday = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
        System.out.println("下一个星期二：" + nextTuesday);

        // 当前月份的第一个星期二的 LocalDate
        LocalDate firstTuesdayInMonth = LocalDate.now()
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.TUESDAY));
        System.out.println("当前月份的第一个星期二的日期：" + firstTuesdayInMonth);

        // 自定义的日期调整器，通过 lambda 表达式调整
        TemporalAdjuster temporalAdjuster = TemporalAdjusters.ofDateAdjuster(w -> {
            LocalDate result = w;
            result = result.plusDays(1);
            return result;
        });
        LocalDate date1 = LocalDate.now()
                .with(temporalAdjuster);
        System.out.println("date1: " + date1);
    }

    // 本地时间（LocalTime），与 LocalDate 使用类似，两个 LocalTime 的间隔是 Duration 类
    @Test
    public void test2() {
        LocalTime nowTime = LocalTime.now();
        System.out.println("当前时间：" + nowTime);
        LocalTime time1 = LocalTime.of(12, 33, 45);
        System.out.println("time1: " + time1);
        LocalTime time2 = time1.plusHours(18);
        System.out.println("time2: " + time2);
        System.out.println("===========================");
        Duration duration = Duration.ofHours(6);
        LocalTime time3 = time1.plus(duration);// 加 Duration 的 plus()
        System.out.println("time3: " + time3);
        int secondOfDay = nowTime.toSecondOfDay();
        System.out.println("当天的第 " + secondOfDay + " 秒");
        boolean isBefore = time1.isBefore(nowTime);
        System.out.println(time1 + "是否在" + nowTime + "之前：" + (isBefore ? "是" : "否"));
    }
}
