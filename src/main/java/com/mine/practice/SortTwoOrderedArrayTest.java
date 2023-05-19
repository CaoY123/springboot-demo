package com.mine.practice;

import java.util.Arrays;

/**
 * @author CaoYang
 * @create 2023-05-17-1:05 PM
 * @description
 * 现给出两个有序整型数组，其中array1按升序（从小到大）排序，array2按降序排序，
 * 请你将 array1和array2 合并到一个新的数组中，并保持新中的元素按升序排序。
 */
public class SortTwoOrderedArrayTest {
    public static void main(String[] args) {

    }

    public static int[] sortTwoOrderedArray(int[] array1, int[] array2) {
        int[] res = null;
        if (array1 == null || array1.length == 0) {
            if (array2 != null) {
                for (int i = array2.length - 1; i >= 0; i--) {
                    res = new int[array2.length];
                }
            }
        }
        if (array2 == null || array2.length == 0) {
            if (array2 != null) {
                res = Arrays.copyOf(array2, array2.length);
            }
        }
    }
}
