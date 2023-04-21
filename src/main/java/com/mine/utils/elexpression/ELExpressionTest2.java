package com.mine.utils.elexpression;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.List;

/**
 * @author CaoYang
 * @create 2023-04-21-9:05 PM
 * @description EL表达式测试2
 * 参考官网：
 */
public class ELExpressionTest2 {

    // EL表达式调用对象属性
    @Test
    public void test1() {
        Person person = new Person("John", 30);
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("name");
        String value = expression.getValue(person, String.class);
        System.out.println("Name: " + value);
    }

    // EL表达式调用对象方法
    @Test
    public void test2() {
        Person person = new Person("John", 50);
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("greet()");
        String value = expression.getValue(person, String.class);
        System.out.println("greet() -> " + value);
    }

    // 使用条件运算符 - 此处是三元运算符
    @Test
    public void test3() {
        // 如果不将局部变量a、b添加到下列上下文中，那么EL表达式就不会认识它们
        int a = 10;
        int b = 20;

        // 创建一个标准的评估上下文，注意，当使用局部变量时EL 表达式的写法，要用#
        StandardEvaluationContext context = new StandardEvaluationContext();
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("#a > #b ? 'a大' : 'b大'");

        // 在上下文中添加a、b
        context.setVariable("a", a);
        context.setVariable("b", b);

        // 使用上下文计算表达式的值
        String value = expression.getValue(context, String.class);
        System.out.println("结果：" + value);
    }

    // 处理集合和数组
    @Test
    public void test4() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        SpelExpressionParser parser = new SpelExpressionParser();
        // 取出值能整除2的数
        Expression expression = parser.parseExpression("#root.?[#this % 2 == 0]");
        List<Integer> value = expression.getValue(list, List.class);
        System.out.println("结果链表：" + value);
    }

    // 而上述展示的知识EL表达式功能的冰山一角，其还可以操作Map、正则表达式等
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Person {
    private String name;
    private int age;

    public String greet() {
        return "Hello, my name is " + name;
    }
}
