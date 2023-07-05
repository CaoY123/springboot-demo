package com.mine.java.time;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author CaoY
 * @date 2023-07-05 23:02
 * @description 日期和时间 API - 时区时间
 *
 * 时区时间很麻烦，还要考虑夏令时的问题，麻了......而且夏令时在有些国家并不施行，因此不是通用标准。
 *
 */
public class DateTimeTest4 {

    @Test
    public void test1() {
        // 获取 IANA 数据库 的可用时区
//        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
//        availableZoneIds.forEach(System.out::println);

        // 获取上海时区的 ZoneId 对象
        ZoneId zoneIdSH = ZoneId.of("Asia/Shanghai");
        ZoneId zoneIdNY = ZoneId.of("America/New_York");
        System.out.println(zoneIdSH);
        System.out.println(zoneIdNY);
        System.out.println("====================================");

        // 上海的当前的时区时间
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTimeSH = ZonedDateTime.of(now, zoneIdSH);
        System.out.println("上海当前的时区时间：" + zonedDateTimeSH);
        Instant instant = zonedDateTimeSH.toInstant(); // 获取的是英国格林尼治时间
        System.out.println("instant: " + instant);
        // UTC 是不考虑夏令时的格林尼治皇家天文台时间
        ZonedDateTime zonedDateTimeLD = instant.atZone(ZoneId.of("UTC"));
        System.out.println("伦敦格林尼治当前的时区时间：" + zonedDateTimeLD);
        ZonedDateTime zonedDateTimeNY = zonedDateTimeSH.withZoneSameInstant(zoneIdNY);
        System.out.println("纽约当前的时区时间：" + zonedDateTimeNY);
        System.out.println("====================================");
    }
}
