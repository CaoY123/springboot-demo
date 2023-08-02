package com.mine.java.compile;

import com.mine.java.compile.anno.MyAnno2;
import org.junit.Test;

/**
 * @author CaoY
 * @date 2023-08-02 14:05
 * @description 脚本、编译与注解处理 - 注解语法
 */
public class CompileTest4 {

    @Test
    public void test1() {
        // 获取子类上的注解信息
        MyAnno2 anno = Son.class.getAnnotation(MyAnno2.class);
        System.out.println("注解的元素值的内容：" + anno.value());
    }

    @MyAnno2("用于测试可继承注解的类")
    private class Father {

    }

    // 子类会继承父类的添加的那些可以继承的注解
    private class Son extends Father {

    }
}
