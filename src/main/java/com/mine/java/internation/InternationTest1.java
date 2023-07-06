package com.mine.java.internation;

import org.junit.Test;

import java.util.Locale;

/**
 * @author CaoY
 * @date 2023-07-07 1:31
 * @description 国际化的学习 - locale
 * 1. locale 可以捕获同一事物在国际间的不同特征（不仅限于语言，也包括表达习惯等），因此在国际化中扮演者重要角色
 * 2. locale 由5个部分组成：
 *  1）一种语言，由2个或3个小写字母表示，比如：en（英语）、zh（中文）；
 *  2）可选的一段脚本，由首大写的4个字母表示，如：Latn（拉丁文）、Hant（繁体中文）；
 *  3）可选的一个国家或地区，由两个大写字母或三个数字表示，如：US（美国）、CN（中国）；
 *  4）可选的一个变体，用于指定各种特点及杂项特性，如各种方言和拼写规则；
 *  5）可选的一个扩展，扩展了日历和数字等内容偏好。
 * 3. locale 是用标签描述的，标签是由 locale 的各个元素通过连字符连接起来的字符串。如：en-US。
 */
public class InternationTest1 {

    @Test
    public void test1() {
        // 获取预定义的语言 Locale
        Locale chinaLocale = Locale.CHINA;
        System.out.println(chinaLocale);
        System.out.println("===============================");

        // 获取所有可用的 Locale
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (Locale availableLocale : availableLocales) {
            System.out.println(availableLocale);
        }
    }

    @Test
    public void test2() {
        // 获取所有语言代码
        String[] isoLanguages = Locale.getISOLanguages();
        for (String isoLanguage : isoLanguages) {
            System.out.println(isoLanguage);
        }
    }

    @Test
    public void test3() {
        String[] isoCountries = Locale.getISOCountries();
        for (String isoCountry : isoCountries) {
            System.out.println(isoCountry);
        }
    }

    @Test
    public void test4() {
        // 获取操作系统的默认 locale
        Locale defaultLocale = Locale.getDefault();
        System.out.println(defaultLocale);

        Locale defaultDisplay = Locale.getDefault(Locale.Category.DISPLAY);
        System.out.println(defaultDisplay);

        Locale defaultFormat = Locale.getDefault(Locale.Category.FORMAT);
        System.out.println(defaultFormat);
    }

    @Test
    public void test5() {
        Locale usLocale = Locale.US;
        String displayName = usLocale.getDisplayName();
        System.out.println(displayName);// 名字以默认的 locale 形式显示出来
        String displayName2 = usLocale.getDisplayName(Locale.US);
        System.out.println(displayName2);// 以我们希望的 locale 来显示
    }

    // locale 相关的例子
    @Test
    public void test6() {
        // 转换为土耳其格式的小写字母（注意字母I的变化）
        String trStr = "DAIT".toLowerCase(Locale.forLanguageTag("tr"));
        System.out.println(trStr);
    }
}
