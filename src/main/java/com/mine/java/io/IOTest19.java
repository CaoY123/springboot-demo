package com.mine.java.io;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CaoY
 * @date 2023-06-25 17:29
 * @description IO流学习 - 正则表达式1
 *
 * 下面主要展示了 Java 的正则表达式的一些基础语法，但遗憾的是，在不同的程序和类库之间，这个语法规则
 * 并不完全统一，是存在一些区别的，这里要当心。
 */
public class IOTest19 {

    @Test
    public void test1() {
        String regex1 = "[Jj]ava.+";
        String str1_1 = "javase";
        System.out.println(str1_1 + " 是否匹配 【" + regex1 + "】：" + (str1_1.matches(regex1) ? "是" : "否"));

        String str1_2 = "Core Java";
        System.out.println(str1_2 + " 是否匹配 【" + regex1 + "】：" + (str1_2.matches(regex1) ? "是" : "否"));

        // 只匹配字符 b
        String regex2 = "b";
        String str2_1 = "b";
        String str2_2 = "m";
        System.out.println(str2_1 + " 是否匹配 【" + regex2 + "】：" + (str2_1.matches(regex2) ? "是" : "否"));
        System.out.println(str2_2 + " 是否匹配 【" + regex2 + "】：" + (str2_2.matches(regex2) ? "是" : "否"));

        // 匹配单个字符
        String regex3 = ".";
        String str3_1 = "a";
        String str3_2 = "]";
        System.out.println(str3_1 + " 是否匹配 【" + regex3 + "】：" + (str3_1.matches(regex3) ? "是" : "否"));
        System.out.println(str3_2 + " 是否匹配 【" + regex3 + "】：" + (str3_2.matches(regex3) ? "是" : "否"));

        // 十六进制码为 10546 的Unicode码点(实际上是字符 F)，这个没有弄懂，不太理解是什么意思
        String regex4 = "\\x{10546}";
        String str4_1 = "F";
        System.out.println(str4_1 + " 是否匹配 【" + regex4 + "】：" + (str4_1.matches(regex4) ? "是" : "否"));

        // 匹配 a、b、C、D 单个字符
        String regex5 = "[abCD]";
        String str5_1 = "a";
        System.out.println(str5_1 + " 是否匹配 【" + regex5 + "】：" + (str5_1.matches(regex5) ? "是" : "否"));

        // 匹配除 a-z、A-Z、0-9 的字符
        String regex6 = "[^a-zA-Z0-9]";
        String str6_1 = "(";
        String str6_2 = "a";
        System.out.println(str6_1 + " 是否匹配 【" + regex6 + "】：" + (str6_1.matches(regex6) ? "是" : "否"));
        System.out.println(str6_2 + " 是否匹配 【" + regex6 + "】：" + (str6_2.matches(regex6) ? "是" : "否"));

        // && 字符集的交集
        String regex7 = "[m&&a-z]";
        String str7_1 = "m";
        String str7_2 = "p";
        System.out.println(str7_1 + " 是否匹配 【" + regex7 + "】：" + (str7_1.matches(regex7) ? "是" : "否"));
        System.out.println(str7_2 + " 是否匹配 【" + regex7 + "】：" + (str7_2.matches(regex7) ? "是" : "否"));

        // 匹配一个 Unicode 字母
        String regex8 = "\\p{L}";
        String str8_1 = "?";
        String str8_2 = "A";
        System.out.println(str8_1 + " 是否匹配 【" + regex8 + "】：" + (str8_1.matches(regex8) ? "是" : "否"));
        System.out.println(str8_2 + " 是否匹配 【" + regex8 + "】：" + (str8_2.matches(regex8) ? "是" : "否"));

        // 匹配个位数，\D 也可以
        String regex9 = "\\d";
        String str9_1 = "5";
        String str9_2 = "b";
        System.out.println(str9_1 + " 是否匹配 【" + regex9 + "】：" + (str9_1.matches(regex9) ? "是" : "否"));
        System.out.println(str9_2 + " 是否匹配 【" + regex9 + "】：" + (str9_2.matches(regex9) ? "是" : "否"));

        // 匹配 a-z、A-Z、0-9，\W 也可以
        String regex10 = "\\w";
        String str10_1 = "K";
        String str10_2 = "7";
        String str10_3 = "j";
        String str10_4 = "{";
        System.out.println(str10_1 + " 是否匹配 【" + regex10 + "】：" + (str10_1.matches(regex10) ? "是" : "否"));
        System.out.println(str10_2 + " 是否匹配 【" + regex10 + "】：" + (str10_2.matches(regex10) ? "是" : "否"));
        System.out.println(str10_3 + " 是否匹配 【" + regex10 + "】：" + (str10_3.matches(regex10) ? "是" : "否"));
        System.out.println(str10_4 + " 是否匹配 【" + regex10 + "】：" + (str10_4.matches(regex10) ? "是" : "否"));

        // 匹配空格，\S 也可以
        String regex11 = "\\s";
        String str11_1 = " ";
        String str11_2 = "a";
        System.out.println(str11_1 + " 是否匹配 【" + regex11 + "】：" + (str11_1.matches(regex11) ? "是" : "否"));
        System.out.println(str11_2 + " 是否匹配 【" + regex11 + "】：" + (str11_2.matches(regex11) ? "是" : "否"));

        // 匹配空白字符：\h、\H：水平空白字符；\v、\V：垂直空白字符
        String regex12 = "\\h";
        String str12_1 = "";
        String str12_2 = " ";
        String str12_3 = "\n";
        System.out.println("空白字符" + " 是否匹配 【" + regex12 + "】：" + (str12_1.matches(regex12) ? "是" : "否"));
        System.out.println(str12_2 + " 是否匹配 【" + regex12 + "】：" + (str12_2.matches(regex12) ? "是" : "否"));
        System.out.println("回车符(\\n)" + " 是否匹配 【" + regex12 + "】：" + (str12_3.matches(regex12) ? "是" : "否"));
    }

    @Test
    public void test2() {
        // 匹配两个字符，第一个字符为 a-z之间的，第二个字符为0-9之间的
        String regex1 = "[a-z][0-9]";
        String str1_1 = "a2";
        String str1_2 = "A2";
        String str1_3 = "a23";
        System.out.println(str1_1 + " 是否匹配 【" + regex1 + "】：" + (str1_1.matches(regex1) ? "是" : "否"));
        System.out.println(str1_2 + " 是否匹配 【" + regex1 + "】：" + (str1_2.matches(regex1) ? "是" : "否"));
        System.out.println(str1_3 + " 是否匹配 【" + regex1 + "】：" + (str1_3.matches(regex1) ? "是" : "否"));

        // X | Y 匹配X或Y中的一种
        String regex2 = "[A-Z]|[0-9]";
        String str2_1 = "G";
        String str2_2 = "5";
        String str2_3 = "b";
        String str2_4 = "C6";
        System.out.println(str2_1 + " 是否匹配 【" + regex2 + "】：" + (str2_1.matches(regex2) ? "是" : "否"));
        System.out.println(str2_2 + " 是否匹配 【" + regex2 + "】：" + (str2_2.matches(regex2) ? "是" : "否"));
        System.out.println(str2_3 + " 是否匹配 【" + regex2 + "】：" + (str2_3.matches(regex2) ? "是" : "否"));
        System.out.println(str2_4 + " 是否匹配 【" + regex2 + "】：" + (str2_4.matches(regex2) ? "是" : "否"));

        // (X)：当正则表达式进行匹配时，如果 (X) 部分与目标字符串匹配成功，
        // 那么匹配到的文本将被保存到一个编号的捕获组中（从左到右依次编号，从 1 开始）。
        // 这些捕获组可以在正则表达式后续的处理中使用。
        String regex3 = "([a-z])([0-9])";
        String str3_1 = "a5";
        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher3 = pattern3.matcher(str3_1);
        if (matcher3.matches()) {
            String group1 = matcher3.group(0);
            String group2 = matcher3.group(1);
            String group3 = matcher3.group(2);
            System.out.println("group1 = " + group1);
            System.out.println("group2 = " + group2);
            System.out.println("group3 = " + group3);
        }
        System.out.println(str3_1 + " 是否匹配 【" + regex3 + "】：" + (str3_1.matches(regex3) ? "是" : "否"));

        /**
         * 1. 正则表达式 ([\'\"]).*\\1 匹配以单引号或双引号开头，然后任意字符（包括空格、标点符号等）出现零次或多次，
         * 最后以与开头引号相同的引号结尾的字符串。
         * 2. ([\'\"])：表示匹配单引号或双引号。使用 [\'\"] 表示字符类，\' 表示单引号，\" 表示双引号。
         * 括号 () 用于分组捕获。
         * 3. .*：表示匹配任意字符（除了换行符）出现零次或多次。. 表示任意字符，* 表示前面的字符可以出现零次或多次。
         * 4. \\1：表示引用第一个捕获组中的内容。\1 指的是与第一个括号 () 中的内容相匹配的部分。
         */
        String regex4 = "([\'\"]).*\\1";
        String str4_1 = "\"Fred\"";
        String str4_2 = "'Fred'";
        String str4_3 = "\"Fred'";
        System.out.println(str4_1 + " 是否匹配 【" + regex4 + "】：" + (str4_1.matches(regex4) ? "是" : "否"));
        System.out.println(str4_2 + " 是否匹配 【" + regex4 + "】：" + (str4_2.matches(regex4) ? "是" : "否"));
        System.out.println(str4_3 + " 是否匹配 【" + regex4 + "】：" + (str4_3.matches(regex4) ? "是" : "否"));

        /**
         * 正则表达式 (?<id>[a-zA-Z0-9]+) 匹配由字母（大小写均可）和数字组成的字符串，
         * 并使用命名捕获组 "id" 将匹配的内容提取出来。
         * 具体解释如下：
         * 1. (?<id>：表示命名捕获组的开始，"id" 是捕获组的名称。
         * 2. [a-zA-Z0-9]+：表示匹配一个或多个字母（包括大小写）或数字。[a-zA-Z0-9] 是字符类，
         * 表示任意字母或数字的范围，+ 表示前面的字符可以出现一次或多次。
         */
        String regex5 = "(?<id>[a-zA-Z0-9]+)";
        String str5_1 = "abcd123KML";
        Pattern pattern5 = Pattern.compile(regex5);
        Matcher matcher5 = pattern5.matcher(str5_1);
        if (matcher5.matches()) {
            String id = matcher5.group("id");
            System.out.println("id = " + id);
        }
    }

    @Test
    public void test3() {
        /**
         * 1. 非捕获组 (?:X) 是正则表达式语法中的一种结构，它用于分组但不捕获匹配结果。
         * 具体来说，它的作用是将括号内的表达式视为一个整体进行匹配，但不将匹配的结果存储在捕获组中。
         * 2. 假设我们有一个字符串 "abc123def456"，我们想要匹配其中连续的数字部分，
         * 但不需要捕获这些数字。我们可以使用 (?:\d+) 来表示该连续数字部分
         * 3. 这里的 (?:\d+) 表示匹配一个或多个数字，并且使用了非捕获组 (?:...)。
         * 由于使用了非捕获组，因此 matcher.group(0) 返回的是完整的匹配结果，而不是特定的捕获组。
         * 4. 总而言之，(?:X) 的用途是在不需要存储匹配结果的情况下，将某个表达式视为一个整体进行匹配。
         */
        String regex1 = "(?:\\d+)";
        String str1_1 = "abc123def456";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(str1_1);
        while (matcher1.find()) {
            System.out.println(matcher1.group(0));
        }

        /**
         * 这个正则表达式 "(?i:jpe?g)" 用于不区分大小写地匹配字符串中的 "jpg" 或者 "jpeg"。
         * 具体来说，它的作用是：
         * 1. (?i:...) 是一个内联修饰符语法，表示接下来的字符或字符组将在不区分大小写的情况下进行匹配。
         * 2. jpe?g 匹配一个 jpg 或 jpeg。
         */
        String regex2 = "(?i:jpe?g)";
        String str2_1 = "jpg";
        String str2_2 = "JPG";
        String str2_3 = "jpEg";
        String str2_4 = "abc.jpG";
        String str2_5 = "mm.jpeg";
        System.out.println(str2_1 + " 是否匹配 【" + regex2 + "】：" + (str2_1.matches(regex2) ? "是" : "否"));
        System.out.println(str2_2 + " 是否匹配 【" + regex2 + "】：" + (str2_2.matches(regex2) ? "是" : "否"));
        System.out.println(str2_3 + " 是否匹配 【" + regex2 + "】：" + (str2_3.matches(regex2) ? "是" : "否"));
        System.out.println(str2_4 + " 是否匹配 【" + regex2 + "】：" + (str2_4.matches(regex2) ? "是" : "否"));
        System.out.println(str2_5 + " 是否匹配 【" + regex2 + "】：" + (str2_5.matches(regex2) ? "是" : "否"));

        /**
         * 正则表达式 X? 可以这样理解：
         * 1. X 是一个表示字符或字符组的表达式。
         * 2. ? 是一个量词，表示前面的表达式 X 出现 0 次或 1 次。
         */
        String regex3 = "A?";
        String str3_1 = "";
        String str3_2 = "A";
        String str3_3 = "AA";
        System.out.println("空字符串" + " 是否匹配 【" + regex3 + "】：" + (str3_1.matches(regex3) ? "是" : "否"));
        System.out.println(str3_2 + " 是否匹配 【" + regex3 + "】：" + (str3_2.matches(regex3) ? "是" : "否"));
        System.out.println(str3_3 + " 是否匹配 【" + regex3 + "】：" + (str3_3.matches(regex3) ? "是" : "否"));

        // 匹配 1 - n个b
        String regex4 = "b+";
        String str4_1 = "";
        String str4_2 = "b";
        String str4_3 = "bb";
        String str4_4 = "bba";
        System.out.println("空字符串" + " 是否匹配 【" + regex4 + "】：" + (str4_1.matches(regex4) ? "是" : "否"));
        System.out.println(str4_2 + " 是否匹配 【" + regex4 + "】：" + (str4_2.matches(regex4) ? "是" : "否"));
        System.out.println(str4_3 + " 是否匹配 【" + regex4 + "】：" + (str4_3.matches(regex4) ? "是" : "否"));
        System.out.println(str4_4 + " 是否匹配 【" + regex4 + "】：" + (str4_4.matches(regex4) ? "是" : "否"));

        // 匹配 0 - n个f
        String regex5 = "f*";
        String str5_1 = "";
        String str5_2 = "f";
        String str5_3 = "ff";
        String str5_4 = "ffg";
        System.out.println("空字符串" + " 是否匹配 【" + regex5 + "】：" + (str5_1.matches(regex5) ? "是" : "否"));
        System.out.println(str5_2 + " 是否匹配 【" + regex5 + "】：" + (str5_2.matches(regex5) ? "是" : "否"));
        System.out.println(str5_3 + " 是否匹配 【" + regex5 + "】：" + (str5_3.matches(regex5) ? "是" : "否"));
        System.out.println(str5_4 + " 是否匹配 【" + regex5 + "】：" + (str5_4.matches(regex5) ? "是" : "否"));
    }

    @Test
    public void test4() {
        // 匹配 3 个r且只能有r
        String regex1 = "r{3}";
        String str1_1 = "rr";
        String str1_2 = "rrr";
        String str1_3 = "rrrr";
        String str1_4 = "rrrr";
        System.out.println(str1_1 + " 是否匹配 【" + regex1 + "】：" + (str1_1.matches(regex1) ? "是" : "否"));
        System.out.println(str1_2 + " 是否匹配 【" + regex1 + "】：" + (str1_2.matches(regex1) ? "是" : "否"));
        System.out.println(str1_3 + " 是否匹配 【" + regex1 + "】：" + (str1_3.matches(regex1) ? "是" : "否"));
        System.out.println(str1_4 + " 是否匹配 【" + regex1 + "】：" + (str1_4.matches(regex1) ? "是" : "否"));

        // 至少匹配 3 个k且只能有k
        String regex2 = "k{3,}";
        String str2_1 = "kk";
        String str2_2 = "kkk";
        String str2_3 = "kkkk";
        String str2_4 = "kkkka";
        System.out.println(str2_1 + " 是否匹配 【" + regex2 + "】：" + (str2_1.matches(regex2) ? "是" : "否"));
        System.out.println(str2_2 + " 是否匹配 【" + regex2 + "】：" + (str2_2.matches(regex2) ? "是" : "否"));
        System.out.println(str2_3 + " 是否匹配 【" + regex2 + "】：" + (str2_3.matches(regex2) ? "是" : "否"));
        System.out.println(str2_4 + " 是否匹配 【" + regex2 + "】：" + (str2_4.matches(regex2) ? "是" : "否"));

        // 匹配p的个数为 2-4 的字符串且字符串只能有p
        String regex3 = "p{2,4}";
        String str3_1 = "p";
        String str3_2 = "pp";
        String str3_3 = "ppp";
        String str3_4 = "pppp";
        String str3_5 = "ppppp";
        String str3_6 = "pppb";
        System.out.println(str3_1 + " 是否匹配 【" + regex3 + "】：" + (str3_1.matches(regex3) ? "是" : "否"));
        System.out.println(str3_2 + " 是否匹配 【" + regex3 + "】：" + (str3_2.matches(regex3) ? "是" : "否"));
        System.out.println(str3_3 + " 是否匹配 【" + regex3 + "】：" + (str3_3.matches(regex3) ? "是" : "否"));
        System.out.println(str3_4 + " 是否匹配 【" + regex3 + "】：" + (str3_4.matches(regex3) ? "是" : "否"));
        System.out.println(str3_5 + " 是否匹配 【" + regex3 + "】：" + (str3_5.matches(regex3) ? "是" : "否"));
        System.out.println(str3_6 + " 是否匹配 【" + regex3 + "】：" + (str3_6.matches(regex3) ? "是" : "否"));

        // 勉强量词：正则表达式的勉强量词是指量词后面加上一个问号 ? 的形式，
        // 表示匹配时尽可能少地重复前面的字符或表达式。
        String regex4 = "A+?";
        String str4_1 = "";
        String str4_2 = "A";
        String str4_3 = "AA";
        String str4_4 = "AAB";
        System.out.println("空字符串" + " 是否匹配 【" + regex4 + "】：" + (str4_1.matches(regex4) ? "是" : "否"));
        System.out.println(str4_2 + " 是否匹配 【" + regex4 + "】：" + (str4_2.matches(regex4) ? "是" : "否"));
        System.out.println(str4_3 + " 是否匹配 【" + regex4 + "】：" + (str4_3.matches(regex4) ? "是" : "否"));
        System.out.println(str4_4 + " 是否匹配 【" + regex4 + "】：" + (str4_4.matches(regex4) ? "是" : "否"));

        // 占有量词：正则表达式的勉强量词是指量词后面加上一个问号 + 的形式，
        // 表示匹配时尽可能多地重复前面的字符或表达式。
        String regex5 = "h{3,4}+";
        String str5_1 = "hh";
        String str5_2 = "hhh";
        String str5_3 = "hhhh";
        String str5_4 = "hhhp";
        System.out.println(str5_1 + " 是否匹配 【" + regex5 + "】：" + (str5_1.matches(regex5) ? "是" : "否"));
        System.out.println(str5_2 + " 是否匹配 【" + regex5 + "】：" + (str5_2.matches(regex5) ? "是" : "否"));
        System.out.println(str5_3 + " 是否匹配 【" + regex5 + "】：" + (str5_3.matches(regex5) ? "是" : "否"));
        System.out.println(str5_4 + " 是否匹配 【" + regex5 + "】：" + (str5_4.matches(regex5) ? "是" : "否"));
    }

    @Test
    public void test5() {
        // 匹配以J开头，a结尾，中间为a-z的0-n个数的字母
        String regex1 = "^J[a-z]*a$";
        String str1_1 = "Ja";
        String str1_2 = "Java";
        String str1_3 = "JDa";
        System.out.println(str1_1 + " 是否匹配 【" + regex1 + "】：" + (str1_1.matches(regex1) ? "是" : "否"));
        System.out.println(str1_2 + " 是否匹配 【" + regex1 + "】：" + (str1_2.matches(regex1) ? "是" : "否"));
        System.out.println(str1_3 + " 是否匹配 【" + regex1 + "】：" + (str1_3.matches(regex1) ? "是" : "否"));

        // 匹配 abc 开头、后面跟 0-n 个数字字符
        String regex2 = "\\Aabc[0-9]*";
        String str2_1 = "abc123";
        String str2_2 = "abc";
        String str2_3 = "ab";
        String str2_4 = "ab12cf";
        System.out.println(str2_1 + " 是否匹配 【" + regex2 + "】：" + (str2_1.matches(regex2) ? "是" : "否"));
        System.out.println(str2_2 + " 是否匹配 【" + regex2 + "】：" + (str2_2.matches(regex2) ? "是" : "否"));
        System.out.println(str2_3 + " 是否匹配 【" + regex2 + "】：" + (str2_3.matches(regex2) ? "是" : "否"));
        System.out.println(str2_4 + " 是否匹配 【" + regex2 + "】：" + (str2_4.matches(regex2) ? "是" : "否"));

        // 匹配以abc结尾、前面有 0-n个数字字符
        String regex3 = "[0-9]*abc\\Z";
        String str3_1 = "ab";
        String str3_2 = "abc";
        String str3_3 = "12abc";
        System.out.println(str3_1 + " 是否匹配 【" + regex3 + "】：" + (str3_1.matches(regex3) ? "是" : "否"));
        System.out.println(str3_2 + " 是否匹配 【" + regex3 + "】：" + (str3_2.matches(regex3) ? "是" : "否"));
        System.out.println(str3_3 + " 是否匹配 【" + regex3 + "】：" + (str3_3.matches(regex3) ? "是" : "否"));

        // 匹配以abc结尾、前面有 0-n个数字字符
        String regex4 = "[0-9]*abc\\z";
        String str4_1 = "ab";
        String str4_2 = "abc";
        String str4_3 = "12abc";
        System.out.println(str4_1 + " 是否匹配 【" + regex4 + "】：" + (str4_1.matches(regex4) ? "是" : "否"));
        System.out.println(str4_2 + " 是否匹配 【" + regex4 + "】：" + (str4_2.matches(regex4) ? "是" : "否"));
        System.out.println(str4_3 + " 是否匹配 【" + regex4 + "】：" + (str4_3.matches(regex4) ? "是" : "否"));
    }

    @Test
    public void test6() {
        // \b 和 \B 是单词边界的标记，用于确保匹配的是 "cat" 这个完整的单词，而不是 "scatter" 中的一部分
        String regex1 = "\\bcat\\b";
        String str1_1 = "I have a cat and a scatter. The cat is cute.";
        // 通过调用 Pattern.compile() 方法，将正则表达式编译为一个 Pattern 对象。
        Pattern pattern1 = Pattern.compile(regex1);
        // 通过调用 Pattern.compile() 方法，将正则表达式编译为一个 Pattern 对象。
        Matcher matcher1 = pattern1.matcher(str1_1);
        while (matcher1.find()) {// 使用 Matcher 对象的 find() 方法开始查找匹配项，如果找到匹配，则进入条件判断。
            System.out.println(matcher1.group());
        }
        System.out.println("***********************");

        String regex2 = "\\Bcat\\B";
        String str2_1 = "I have a cat and catch a scatter. The cats are cute.";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(str2_1);
        while (matcher2.find()) {
            System.out.println(matcher2.group());
        }
        System.out.println("***********************");

        // 下面的例子中，对于 regex3_1 中的 [a-z]* 会匹配到 cab 中的 c，而剩下的 可以匹配到 ab；
        // 而对于 regex3_2 中的 [a-z]*+ 贪婪匹配会将 cab 的全部字符一次性匹配到，那剩下的 ab 没有
        // 办法匹配到，因此整体上来说匹配就失败了，所以有时候贪婪匹配会使整个匹配失败。
        String regex3_1 = "[a-z]*ab";
        String regex3_2 = "[a-z]*+ab";
        String str3_1 = "cab";
        System.out.println(str3_1 + " 是否匹配 【" + regex3_1 + "】：" + (str3_1.matches(regex3_1) ? "是" : "否"));
        System.out.println(str3_1 + " 是否匹配 【" + regex3_2 + "】：" + (str3_1.matches(regex3_2) ? "是" : "否"));

        // 匹配十进制和十六进制数
        String regex4 = "[+-]?[0-9]+|0[Xx][0-9A-Fa-f]+";
        String str4_1 = "123";
        String str4_2 = "+32";
        String str4_3 = "-76";
        String str4_4 = "0X3a";
        String str4_5 = "0x2F";
        String str4_6 = "0X8";
        System.out.println(str4_1 + " 是否匹配 【" + regex4 + "】：" + (str4_1.matches(regex4) ? "是" : "否"));
        System.out.println(str4_2 + " 是否匹配 【" + regex4 + "】：" + (str4_2.matches(regex4) ? "是" : "否"));
        System.out.println(str4_3 + " 是否匹配 【" + regex4 + "】：" + (str4_3.matches(regex4) ? "是" : "否"));
        System.out.println(str4_4 + " 是否匹配 【" + regex4 + "】：" + (str4_4.matches(regex4) ? "是" : "否"));
        System.out.println(str4_5 + " 是否匹配 【" + regex4 + "】：" + (str4_5.matches(regex4) ? "是" : "否"));
        System.out.println(str4_6 + " 是否匹配 【" + regex4 + "】：" + (str4_6.matches(regex4) ? "是" : "否"));
    }
}
