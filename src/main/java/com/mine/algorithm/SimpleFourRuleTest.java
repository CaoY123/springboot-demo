package com.mine.algorithm;

import io.swagger.models.auth.In;

import java.util.Stack;

/**
 * 简易的四则运算模拟，只模拟加减乘除，没有括号，而且保证计算的结果是正整数，
 * 即使除不尽也只保留整数部分，中间过程中也是如此。
 * 不确定一定正确，如有错误，欢迎指正。
 */
public class SimpleFourRuleTest {
    public static void main(String[] args) {
//        String expression = "6+2*5";
//        String expression = "2*4/3";
//        String expression = "7/3";
        String expression = "10 - 3 + 6";
//        String expression = "2 * 3 * 6";
        System.out.println(calc(expression));
    }

    public static int calc (String expression) {
        Stack<Integer> numStack = new Stack<>();
        Stack<Character> optStack = new Stack<>();

        int num = 0;
        boolean flag = false;
        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            if (ch == ' ') {
                if (flag) {
                    numStack.push(num);
                    num = 0;
                    flag = false;
                }
                continue;
            } else if (ch >= '0' && ch <= '9') {
                num = num * 10 + (ch - 48);
                flag = true;
                if (i == expression.length() - 1) {
                    numStack.push(num);
                }
            } else {
                if (flag) {
                    numStack.push(num);
                    num = 0;
                    flag = false;
                }
                if (optStack.isEmpty() || isPre(optStack.peek(), ch)) {
                    optStack.push(ch);
                } else {
                    while (!optStack.isEmpty() && !isPre(optStack.peek(), ch)) {
                        // 计算
                        int num1 = numStack.pop();
                        int num2 = numStack.pop();
                        char opt = optStack.pop();
                        int tmpResult = singleCalc(num2, num1, opt);
                        numStack.push(tmpResult);
                    }
                    optStack.push(ch);
                }
            }
        }
        double res = 0;
        while (!optStack.isEmpty()) {
            char opt = optStack.pop();
            int num1 = numStack.pop();
            int num2 = numStack.pop();
            numStack.push(singleCalc(num2, num1, opt));
        }
        res = numStack.pop();
        return (int) res;
    }

    public static boolean isPre(char a, char b) {
        if (a == '+' || a == '-') {
            if (b == '+' || b == '-') {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static int singleCalc(int num1, int num2, char opt) {
        int tmpResult = 0;
        switch (opt) {
            case '+':
                tmpResult = num1 + num2;
                break;
            case '-':
                tmpResult = num1 - num2;
                break;
            case '*':
                tmpResult = num1 * num2;
                break;
            case '/':
                tmpResult = num1 / num2;
                break;
        }
        return tmpResult;
    }
}