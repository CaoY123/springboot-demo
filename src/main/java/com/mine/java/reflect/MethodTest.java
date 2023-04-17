package com.mine.java.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author CaoY
 * @date 2023-04-18 0:04
 * @description 反射Method测试类
 */
public class MethodTest {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = Class.forName("com.mine.java.reflect.Circle");

        // getMethods()获取public的方法，但是也包括父类的方法
        Method method = clazz.getMethod("draw", int.class, String.class);

        System.out.println("method: " + method);
        System.out.println("====================================");

        // 获取所有的public方法：
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            System.out.println("m1: " + m);
        }
        System.out.println("====================================");

        // getDeclaredMethods() 获取本类的所有方法，但不包括父类的方法
        Method method1 = clazz.getDeclaredMethod("drawCircle");
        System.out.println("method1: " + method1);
        System.out.println("====================================");

        Method[] methods1 = clazz.getDeclaredMethods();
        for (Method m : methods1) {
            System.out.println("m2: " + m);
        }
        System.out.println("====================================");

        Circle circle = (Circle) clazz.newInstance();
        method1.setAccessible(true);
        method1.invoke(circle);
        System.out.println("====================================");

        method.invoke(circle, 18, "圆圈");

        Method method2 = clazz.getDeclaredMethod("getAllCount");
        int allCount = (Integer) method2.invoke(circle);
        System.out.println("allCount: " + allCount);
        System.out.println("====================================");

        System.out.println("method2.returnType: " + method2.getReturnType());
        System.out.println("method1.returnType: " + method1.getReturnType());
        System.out.println("====================================");

        Type genericReturnType = method2.getGenericReturnType();
        System.out.println(genericReturnType);
        System.out.println("====================================");

        System.out.println("是否有可变参数：" + method1.isVarArgs());
    }
}

class Shape {
    public void draw(){
        System.out.println("draw");
    }

    public void draw(int count , String name){
        System.out.println("draw "+ name +",count="+count);
    }

}
class Circle extends Shape {

    private void drawCircle() {
        System.out.println("drawCircle");
    }

    public int getAllCount() {
        return 100;
    }
}
