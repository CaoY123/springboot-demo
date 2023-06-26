package com.mine.java.io;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-26 17:08
 * @description IO流学习 - 正则表达式2
 * 正则表达式使用的一些例子，这一块的测试用例并不是我自己写的，是人工智能帮我完成的，这块的语法我并不熟悉。
 */
public class IOTest20 {

    @Test
    public void test1() {
        // 虽然正则表达式中不匹配大写字母，但是编译的时候设置了不区分大小写，所以也可以匹配有大写字符的字符串
        String regex1 = "[a-z0-9]+";
        String str1_1 = "abcd123KL";
        // 编译模式时，设置一些标志。
        Pattern pattern1 = Pattern.compile(regex1, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher1 = pattern1.matcher(str1_1);
        System.out.println(str1_1 + " 是否匹配 【" + regex1 + "】：" + (matcher1.matches() ? "是" : "否"));

        String regex2 = "[a-zA-Z0-9]+";
        String[] oldStrArray = {"abc123", "def456", "xyz789", "!@as#$%", "#@$^&"};

        // 实际上，在 pattern2.asPredicate() 方法中，只要字符串中存在与给定模式匹配的子字符串，
        // 就会保留该字符串，而不是完全符合模式的字符串。因此，在示例代码中，
        // 尽管字符串 "!@as#$%" 中包含与模式匹配的子字符串 "as"，但整个字符串也会被保留下来。
        Pattern pattern2 = Pattern.compile(regex2);
        Predicate<String> predicate = pattern2.asPredicate();

        Stream<String> stream = Stream.of(oldStrArray);
        String[] newStrArray = stream.filter(predicate).toArray(String[]::new);
        System.out.println("原字符串数组：" + Arrays.toString(oldStrArray));
        System.out.println("新字符串数组：" + Arrays.toString(newStrArray));
    }

    @Test
    public void test2() {
        String input = "Hello, my email address is john.doe@example.com.";

        // 正则表达式匹配电子邮件地址
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 获取匹配项的起始位置和结束位置
            int start = matcher.start();
            int end = matcher.end();

            // 提取匹配项的内容
            String match = input.substring(start, end);

            System.out.println("Found: " + match + " (start: " + start + ", end: " + end + ")");
        }
    }

    // 正则匹配分组
    @Test
    public void test3() {
        String regex = "(([1-9]|1[0-2]):([0-5][0-9]))[ap]m";
        Pattern pattern = Pattern.compile(regex);
        String input = "11:59am";
        Matcher matcher = pattern.matcher(input);
        int groupCount = matcher.groupCount();
        System.out.println("组数：" + groupCount);
        if (matcher.matches()) {// 试图将字符串与正则表达式匹配
            for (int i = 0; i <= groupCount; i++) {
                System.out.println("索引：" + i + "，开始：" + matcher.start(i) + "，结束：" + matcher.end(i) + "，字符串：" + matcher.group(i));
            }
        } else {
            System.out.println("No Match.");
        }
    }

    @Test
    public void test4() {
        String input = "The quick brown fox jumps over the lazy dog.";

        // 正则表达式匹配连续的三个单词
        String regex = "\\b(\\w+\\s+\\w+\\s+\\w+)\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            // 开始下标
            int start = matcher.start();
            // 结束下标
            int end = matcher.end();
            // 匹配的项
            String match = matcher.group();

            System.out.println("Found: " + match + " (start: " + start + ", end: " + end + ")");
        }
    }

    // 使用正则表达式分割字符串
    @Test
    public void test5() {
        String regex = "[,|\\-]";
        String input = "Hello,World|Java-Programming";
        String[] str = input.split(regex);
        System.out.println(Arrays.toString(str));
    }

    // 使用正则表达式替换部分字符串
    @Test
    public void test6() {
        // 1. 替换第一个重复的字母
        String input = "Heeellooo";
        String regex = "(.)\\1+"; // 匹配连续重复的字母
        String replacement = "$1"; // 替换为第一个重复的字母
        String result = input.replaceFirst(regex, replacement);
        System.out.println("替换前：" + input);
        System.out.println("替换后：" + result); // 输出: Hellooo
        System.out.println("******************************");

        // 2. 替换特定格式的日期
        String input2 = "Today is 2023-06-26";
        String regex2 = "(\\d{4})-(\\d{2})-(\\d{2})"; // 匹配格式为YYYY-MM-DD的日期
        String replacement2 = "$3/$2/$1"; // 以DD/MM/YYYY的格式进行替换
        String result2 = input2.replaceAll(regex2, replacement2);
        System.out.println("替换前：" + input2);
        System.out.println("替换后：" + result2); // 输出: Today is 26/06/2023
        System.out.println("******************************");

        // 3. 替换连续的空格为单个空格
        String input3 = "Hello       World";
        String regex3 = "\\s+"; // 匹配连续的空格
        String replacement3 = " "; // 替换为单个空格
        String result3 = input3.replaceAll(regex3, replacement3);
        System.out.println("替换前：" + input3);
        System.out.println("替换后：" + result3); // 输出: Hello World
    }
}
