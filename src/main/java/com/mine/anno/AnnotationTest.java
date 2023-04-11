package com.mine.anno;

import com.mine.anno.annotation.TestInheritedAnnotation;
import org.junit.Test;

import java.lang.annotation.Annotation;

/**
 * @author CaoY
 * @date 2023-04-12 0:44
 * @description 注解测试
 */
public class AnnotationTest {

    @Test
    public void test1() {

        Student student = new Student();
        student.test();
    }

}

// 用于测试 @Inherited 注解，下面的子类会继承@TestInheritedAnnotation 注解，
// 因为@TestInheritedAnnotation 被 @Inherited修饰
@TestInheritedAnnotation(values = {"value"}, number = 10)
class Person {

}

class Student extends Person{

    public void test(){
        Class clazz = Student.class;
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation.toString());
        }
    }
}
