package com.mine.utils;

import cn.hutool.core.date.DateUtil;
import com.mine.enums.Formatter;

import java.util.Date;

/**
 * @author CaoYang
 * @create 2023-04-21-1:58 PM
 * @description Date的工具类测试
 */
public class DateUtilTest {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);

        String dateFormat = DateUtil.format(date, Formatter.YYYYMMDDHHMMSS.getPattern());
        System.out.println(dateFormat);
    }
}
