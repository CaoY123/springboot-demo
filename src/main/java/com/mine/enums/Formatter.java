package com.mine.enums;

import java.time.format.DateTimeFormatter;

/**
 * @author CaoYang
 * @date 2023-04-23 14:01:43
 * @description
 */
public enum Formatter {
    YYYY_MM_DD("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd")),

    YYYYMMDD("yyyyMMdd", DateTimeFormatter.ofPattern("yyyyMMdd")),

    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),

    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),

    YYYYMMDD_HH_MM_SS("yyyyMMdd HH:mm:ss", DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")),

    YYYY_MM_DD_HH_MM_SS_SSS("yyyy-MM-dd HH:mm:ss.SSS", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),

    YYYY_MM_DD_HH_MM_SS_S("yyyy-MM-dd HH:mm:ss.S", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")),

    YYYYMMDDHHMMSS("yyyyMMddHHmmss", DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),

    YYYYMMDD_HHMMSS("yyyyMMdd:HHmmss", DateTimeFormatter.ofPattern("yyyyMMdd:HHmmss")),

    YYYYMMDDHHMMSSSSS("yyyyMMddHHmmssSSS", DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")),

    HHMMSSSSS("HHmmssSSS", DateTimeFormatter.ofPattern("HHmmssSSS")),

    YYYY$MM$DD("yyyy/MM/dd", DateTimeFormatter.ofPattern("yyyy/MM/dd")),

    HHMMSS("HHmmss", DateTimeFormatter.ofPattern("HHmmss")),

    HH_MM_SS("HH:mm:ss", DateTimeFormatter.ofPattern("HH:mm:ss")),

    YYYY$MM$DD_HH_MM("yyyy/MM/dd HH:mm", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")),

    YYYY$MM$DD_HH_MM_SS("yyyy/MM/dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),

    YYYY("yyyy", DateTimeFormatter.ofPattern("yyyy")),

    MM("MM", DateTimeFormatter.ofPattern("MM")),

    DD("dd", DateTimeFormatter.ofPattern("dd")),

    YYYYMM("yyyyMM", DateTimeFormatter.ofPattern("yyyyMM")),
    ;

    private String pattern;

    private DateTimeFormatter formatter;

    Formatter(String pattern, DateTimeFormatter formatter) {
        this.pattern = pattern;
        this.formatter = formatter;
    }

    public String getPattern() {
        return pattern;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}