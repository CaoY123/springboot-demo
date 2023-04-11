package com.mine.generic;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * @author CaoY
 * @date 2023-04-11 21:57
 * @description 关于泛型的测试
 * 统一的参考链接：https://pdai.tech/md/java/basic/java-basic-x-generic.html
 */
public class GenericTest {

    // 证明泛型擦除存在
    @Test
    public void test1() {
        ArrayList<String> list1 = new ArrayList<String>();
        list1.add("abc");

        ArrayList<Integer> list2 = new ArrayList<Integer>();
        list2.add(123);

        System.out.println(list1.getClass() == list2.getClass()); // true
    }

    // 证明泛型擦除存在
    @Test
    public void test2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<Integer> list = new ArrayList<Integer>();

        list.add(1);  //这样调用 add 方法只能存储整形，因为泛型类型的实例为 Integer

        list.getClass().getMethod("add", Object.class).invoke(list, "asd");

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }

    @Test
    public void test3() {
        /**不指定泛型的时候*/
        int i = GenericTest.add(1, 2); //这两个参数都是Integer，所以T为Integer类型
        Number f = GenericTest.add(1, 1.2); //这两个参数一个是Integer，一个是Float，所以取同一父类的最小级，为Number
        Object o = GenericTest.add(1, "asd"); //这两个参数一个是Integer，一个是String，所以取同一父类的最小级，为Object

        /**指定泛型的时候*/
        int a = GenericTest.<Integer>add(1, 2); //指定了Integer，所以只能为Integer类型或者其子类
//        int b = GenericTest.<Integer>add(1, 2.2); //编译错误，指定了Integer，不能为Float
        Number c = GenericTest.<Number>add(1, 2.2); //指定为Number，所以可以为Integer和Float
    }

    // 类型检查就是针对引用的，谁是一个引用，用这个引用调用泛型方法，
    // 就会对这个引用调用的泛型方法进行类型检测，而无关它真正引用的对象
    @Test
    public void test4() {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("1");// 编译通过
//        list1.add(1);// 编译错误
        String str1 = list1.get(0);// 返回类型是String

        ArrayList list2 = new ArrayList<String>();
        list2.add("1");// 编译通过
        list2.add(1);// 编译通过
        Object object = list2.get(0);

        new ArrayList<String>().add("11");//编译通过
//        new ArrayList<String>().add(22);// 编译错误

        String str2 = new ArrayList<String>().get(0); // 返回类型就是String
    }

    private static <T> T add(T x, T y) {
        return y;
    }
}
