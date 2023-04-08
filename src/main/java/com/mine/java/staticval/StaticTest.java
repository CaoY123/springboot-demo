package com.mine.java.staticval;

import com.mine.java.staticval.entity.StaticA;
import com.mine.java.staticval.entity.StaticB;
import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-04-08 15:25
 * @description 关于含有静态部分类的初始化
 */
public class StaticTest {

    @Test
    public void test1() {

        StaticA staticA = new StaticA();
    }

    @Test
    public void test2() {

        StaticB staticB = new StaticB();
    }
}
