package com.mine.java.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author CaoY
 * @date 2023-04-17 23:16
 * @description java 反射构造器类
 */
public class ConstructorTest {

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = null;

        //获取Class对象的引用
        clazz = Class.forName("com.mine.java.reflect.User2");

        //第一种方法，实例化默认构造方法，User必须无参构造函数,否则将抛异常
        User2 user = (User2) clazz.newInstance();
        user.setAge(20);
        user.setName("Jack");
        System.out.println(user);

        System.out.println("--------------------------------------------");

        //获取带String参数的public构造函数
        Constructor cs1 =clazz.getConstructor(String.class);
        //创建User
        User2 user1= (User2) cs1.newInstance("hiway");
        user1.setAge(22);
        System.out.println("user1:"+user1.toString());

        System.out.println("--------------------------------------------");

        //取得指定带int和String参数构造函数,该方法是私有构造private
        Constructor cs2=clazz.getDeclaredConstructor(int.class, String.class);
        //由于是private必须设置可访问
        cs2.setAccessible(true);
        //创建user对象
        User2 user2= (User2) cs2.newInstance(25,"hiway2");
        System.out.println("user2:" + user2.toString());

        System.out.println("--------------------------------------------");

        //获取所有构造包含private
        Constructor<?> cons[] = clazz.getDeclaredConstructors();
        // 查看每个构造方法需要的参数
        for (int i = 0; i < cons.length; i++) {
            //获取构造函数参数类型
            Class<?> clazzs[] = cons[i].getParameterTypes();
            System.out.println("构造函数["+i+"]:"+cons[i].toString() );
            System.out.print("参数类型["+i+"]:(");
            for (int j = 0; j < clazzs.length; j++) {
                if (j == clazzs.length - 1) {
                    System.out.print(clazzs[j].getName());
                }
                else {
                    System.out.print(clazzs[j].getName() + ",");
                }
            }
            System.out.println(")");
        }

        System.out.println("--------------------------------------------");

        Class declaringClass = cs1.getDeclaringClass();
        System.out.println(declaringClass);
        System.out.println("--------------------------------------------");

        Type[] genericParameterTypes = cs2.getGenericParameterTypes();
        System.out.println(Arrays.toString(genericParameterTypes));
        Type[] genericParameterTypes1 = cs1.getGenericParameterTypes();
        System.out.println(Arrays.toString(genericParameterTypes1));

        System.out.println("--------------------------------------------");
        System.out.println(cs2.toGenericString());
        System.out.println("--------------------------------------------");
        System.out.println(cs1.getName());
        System.out.println("--------------------------------------------");
        User2 user21 = (User2) cs2.newInstance(20, "Mike");
        System.out.println(user21);

    }
}

class User2 {
    private int age;
    private String name;
    public User2() {
        super();
    }
    public User2(String name) {
        super();
        this.name = name;
    }

    /**
     * 私有构造
     * @param age
     * @param name
     */
    private User2(int age, String name) {
        super();
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User2{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
