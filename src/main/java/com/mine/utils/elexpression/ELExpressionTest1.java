package com.mine.utils.elexpression;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author CaoYang
 * @create 2023-04-21-6:26 PM
 * @description EL表达式测试 - Expression
 */
public class ELExpressionTest1 {

    @Test
    public void test1() {
        // 创建一个 ExpressionParser 实例来解析表达式
        ExpressionParser parser = new SpelExpressionParser();

        // 使用 ExpressionParser 解析一个表达式字符串，得到一个 Expression 实例
        Expression expression = parser.parseExpression("'Hello, World!'");

        // 计算表达式的值
        String result = expression.getValue(String.class);

        // 输出结果
        System.out.println(result); // 输出 "Hello, World!"
    }

    @Test
    public void test2() {
        // 创建一个EL表达式解析器
        SpelExpressionParser parser = new SpelExpressionParser();

        // 使用解析器来解析字符串
        Expression expression = parser.parseExpression("3 * (1 + 2)");

        // 计算表达式的值
        int result = expression.getValue(Integer.class);

        // 输出结果：
        System.out.println("四则运算的结果：" + result);
    }
}
