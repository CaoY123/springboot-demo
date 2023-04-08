package com.mine.java.calculate;

import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-04-08 16:26
 * @description 计算测试类
 */
public class CalcTest {

    // 主要是展示 b = a + b 和 b += a的区别，前者涉及向上类型提升，后者涉及强转
    @Test
    public void test1() {
        byte a = 127;
        byte b = 127;
        b += a; // 编译可以通过，但结果出错，涉及到强转
//        b = a + b; // 编译不可通过，因为a + b会将二者的结果的类型向上提升为int，将一个int赋值给byte自然就会报错
        System.out.println("b = " + b);
    }

    @Test
    public void test2() {
        // 下列输出false，因为有些浮点数不能精确的表示出来
        System.out.println(3 * 0.1 == 0.3);
    }


}
