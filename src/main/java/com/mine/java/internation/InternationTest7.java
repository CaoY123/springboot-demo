package com.mine.java.internation;

import org.junit.Test;

import java.text.MessageFormat;
import java.time.LocalDate;

/**
 * @author CaoY
 * @date 2023-07-10 14:16
 * @description 国际化的学习 - 消息格式化
 */
public class InternationTest7 {

    @Test
    public void test1() {
        String msg = MessageFormat.format("On {2}, a {0} destroyed {1} houses and caused {3} of damage.",
                "hurricane", 99, LocalDate.of(1999, 3, 2), 10.0E8);
        System.out.println(msg);

        String msg2 = MessageFormat.format("On {2}, a {0} destroyed {1} houses and caused {3,number,currency} of damage.",
                "hurricane", 99, LocalDate.of(1999, 3, 2), 10.0E8);
        System.out.println(msg2);
    }

    /**
     * 选择格式：解决单复数描述的问题
     * 格式化选择器就是为了解决这样的问题，它由多个序列对构成，每一个序列对包括：
     *  1. 一个下限；
     *  2. 一个格式字符串；
     *  3. 下限和格式化字符串之间由一个 # 分隔，序列对之间由 | 分隔
     */
    @Test
    public void test2() {
        // 没有房子被摧毁
        String msg1 = MessageFormat.format("On {2}, a {0} destroyed {1,choice,0#no houses|1#one house|2#{1}houses} and caused {3,number,currency} of damage.",
                "hurricane", 0, LocalDate.of(1999, 3, 2), 10.0E8);
        System.out.println(msg1);

        // 1 幢房子被摧毁
        String msg2 = MessageFormat.format("On {2}, a {0} destroyed {1,choice,0#no houses|1#one house|2#{1}houses} and caused {3,number,currency} of damage.",
                "hurricane", 1, LocalDate.of(1999, 3, 2), 10.0E8);
        System.out.println(msg2);

        // 2幢房子被摧毁
        String msg3 = MessageFormat.format("On {2}, a {0} destroyed {1,choice,0#no houses|1#one house|2#{1} houses} and caused {3,number,currency} of damage.",
                "hurricane", 2, LocalDate.of(1999, 3, 2), 10.0E8);
        System.out.println(msg3);

        // 多幢房子被摧毁
        String msg4 = MessageFormat.format("On {2}, a {0} destroyed {1,choice,0#no houses|1#one house|2#{1} houses} and caused {3,number,currency} of damage.",
                "hurricane", 25, LocalDate.of(1999, 3, 2), 10.0E8);
        System.out.println(msg4);
    }
}
