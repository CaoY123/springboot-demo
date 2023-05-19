package com.mine.practice;

/**
 * @author CaoYang
 * @create 2023-05-17-12:54 PM
 * @description
 * 对于给定的非负整数r和n（r≤n），请编写程序计算组合数C(r,n) = n! / r! / (n-r)!。
 * （本题目用于测试的所有用例，都保证结果小于2^31-1）
 * 解法：需要应用公式：C(r, n) = C(r, n - 1) + C(r - 1, n - 1)
 */
public class CalcCombinationTest {
    public static void main(String[] args) {
        int r = 2;
        int n = 5;
        System.out.println(combination(r, n));
    }

    public static int combination(int r, int n) {

        if (n == r || n <= 1 || r == 0) {
            return 1;
        }

        if (r > n / 2) {
            return combination(n - r, n);
        }

        return combination(r, n - 1) + combination(r - 1, n - 1);
    }
}
