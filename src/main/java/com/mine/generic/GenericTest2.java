package com.mine.generic;

import org.junit.Test;

import java.util.ArrayList;

/**
 * @author CaoY
 * @date 2023-04-11 22:15
 * @description 关于泛型的测试2
 */
public class GenericTest2 {

    @Test
    public void test1() {
        // 下面两种形式不允许
//        ArrayList<String> list1 = new ArrayList<Object>(); // 编译错误
//        ArrayList<Object> list2 = new ArrayList<String>();// 编译错误

        // 上面情况1的扩展
        ArrayList<Object> list1 = new ArrayList<Object>();
        list1.add(new Object());
        list1.add(new Object());
        // 如果下面的语句编译允许，那么当我们使用list2的时候，如：list2.get(0)，
        // 我们希望得到的是字符串，但是实际里面存放的却是Object对象，而Object显然不能直接转为
        // String，所以Java不能允许这样的语句，否则就违背了泛型的初衷
//        ArrayList<String> list2 = list1;// 编译错误

        // 上面第二种情况的扩展
        ArrayList<String> list3 = new ArrayList<String>();
        list3.add(new String());
        list3.add(new String());
        // 显然下面的语句不存在类型转换的问题（因为是String -> Object），
        // 但是这么做意义不大，使用的时候还是需要强制转换，这显然违背了泛型的设计初衷
//        ArrayList<Object> list4 = list3;// 编译报错
    }



}
