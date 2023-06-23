package com.mine.java.io;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import lombok.Data;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;

/**
 * @author CaoY
 * @date 2023-06-23 12:03
 * @description IO流学习 - 使用序列化来克隆
 * 使用 ObjectOutputStream 和 ByteArrayOutputStream 将原对象序列化，
 * 再用 ObjectInputStream 和 ByteArrayInputStream 反序列化已序列化的对象，
 * 由于反序列化会生成一个新的对象，所以正好实现了深拷贝的功能。而且还不需要知道要拷贝的类的结构。
 */
public class IOTest14 {

    // 可拷贝和序列化的类
    private static class SerialCloneable implements Cloneable, Serializable{

        public static final long serialVersionUID = -3962364661247805043L;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(this);

                try (ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray())) {
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    return ois.readObject();
                }
            } catch (IOException | ClassNotFoundException e) {
                CloneNotSupportedException e2 = new CloneNotSupportedException();
                // initCause 用于为一个异常设置原因，它允许我们将一个异常标记为另一个异常的原因，从而形成异常链
                // 从而可以通过 getCause() 来追溯到异常的原因，但是要原因异常不能行程循环链，即如果异常A为异常B
                // 的原因，又将异常B设置为异常A的原因，这样就会出现无限循环的异常连。
                e2.initCause(e);
                throw e2;
            }
        }
    }

    @Data
    private static class Employee extends SerialCloneable {

        private Integer id;
        private String name;
        private Double salary;
        private LocalDate hireDay;

        public Employee() {

        }

        public Employee(Integer id, String name, Double salary, LocalDate hireDay) {
            this.id = id;
            this.name = name;
            this.salary = salary;
            this.hireDay = hireDay;
        }
    }

    @Data
    private static class Animal extends SerialCloneable{
        private String name;
        private Integer age;

        public Animal() {

        }

        public Animal(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    @Test
    public void test1() throws CloneNotSupportedException {
        Employee employee1 = new Employee(1001, "张三", 32000.0, LocalDate.of(2000, 5, 17));
        System.out.println("employee1: " + employee1);
        Employee employee2 = (Employee) employee1.clone();
        System.out.println("employee2: " + employee2);
        System.out.println("两个对象内存地址是否相同：" + (employee1 == employee2 ? "是" : "否"));
    }

    @Test
    public void test2() throws CloneNotSupportedException {
        Animal animal1 = new Animal("老虎", 12);
        System.out.println("animal1: " + animal1);
        Animal animal2 = (Animal) animal1.clone();
        System.out.println("animal2: " + animal2);
        System.out.println("两个 Animal 对象的内存地址是否相同：" + (animal1 == animal2 ? "是" : "否"));
    }
}
