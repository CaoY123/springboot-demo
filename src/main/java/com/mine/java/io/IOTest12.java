package com.mine.java.io;

import lombok.Data;
import org.junit.Test;

import java.io.*;

/**
 * @author CaoY
 * @date 2023-06-21 10:44
 * @description IO流学习 - 序列化单例和类型安全的枚举 - 覆盖readResolve(private或protected的)
 * 下面写了一个枚举类 Orientation，可以看到，枚举类只能创建出 HORIZONTAL 和 VERTICAL，而且创建出的这两种不同
 * 的对象均是单例的，即它们各自用于相同的内存地址。但是，在反序列化的过程中，使用ObjectOutputStream的readObject()
 * 重新构建对象时，会重新创建出一个新的 Orientation 对象，而不是使用原始对象。因此反序列化得到的对象与原始对象不同，
 * 它们拥有不同的地址，即使它们的状态信息完全相同。
 *
 * 关于 Java 中反序列化过程的探究：
 * 这是因为在Java中，如果一个类的所有构造函数都是私有的或者受保护的，Java反序列化机制会尝试使用特殊的机制创建
 * 新的对象实例，而不是调用构造函数来创建。在这种情况下，Java使用Unsafe.allocateInstance()方法手动分配
 * 内存，并直接设置对象头来创建对象实例。然后，Java反序列化机制会使用反射机制来访问对象的成员变量，并将读取到
 * 的字节序列中的数据赋值给这些成员变量。
 *
 * 实际上，上面的关于Java反序列化可以深入探究，即：
 * 反序列化就是将二进制数据流转化为Java对象的过程，反序列化首先要找到相应的字段和值，并为对象赋值。首先通过调用对象的
 * 构造函数来创建对象，并将读取到的二进制流中的数据填充到对象的相应字段中完成呢对象的初始化，构造函数在这个过程中只是
 * 创建对象的载体，而不影响反序列化的行为。
 * 如果对象的类没有无参构造函数，那么Java反序列化机制必须使用带参数的构造函数创建对象实例，并将读取到的数据传递给它们
 * 来完成对象初始化。而即使在Java类中我们一个构造函数也不提供，JVM也会生成一个默认的空参构造函数，所以不会出现构造
 * 函数带来的反序列化不成功的情况。
 *
 * 要解决反序列化时创建新对象带来的非单例问题，需要重写 readResolve()，一般将其设置为 private 的，为了保证不被
 * 外面调用，而由反序列化时通过反射调用，这样可以保证单例不被破坏
 */
public class IOTest12 {

    // Orientation(取向、方向类)，只可能取 HORIZONTAL(水平)、VERTICAL(垂直)两个值
    private static class Orientation implements Serializable {

        public static final Orientation HORIZONTAL = new Orientation(1);// 水平方向
        public static final Orientation VERTICAL = new Orientation(2);// 垂直方向

        private int value;

        private Orientation(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Orientation{" +
                    "value=" + value +
                    '}';
        }
    }

    // 测试上述 Orientation 枚举类的
    @Test
    public void test1() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        Orientation horizontal = Orientation.HORIZONTAL;
        Orientation vertical = Orientation.VERTICAL;
        oos.writeObject(horizontal);
        oos.writeUTF("\n");
        oos.writeObject(vertical);
        oos.writeObject(horizontal);// 再写入一个 horizontal 对象
        oos.close();
        System.out.println("================写入结束================");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Orientation orientation1 = (Orientation) ois.readObject();
        System.out.print(orientation1);
        String s = ois.readUTF();
        System.out.print(s);
        Orientation orientation2 = (Orientation) ois.readObject();
        System.out.println(orientation2);
        Orientation orientation3 = (Orientation) ois.readObject();
        System.out.println(orientation3);
        ois.close();
        // 经过测试，上面的读取和写入没有任何问题。
        System.out.println("=====================================");
        // 可以看到，上面的枚举类是单例的
        Orientation horizontal2 = Orientation.HORIZONTAL;
        System.out.println("Orientation是单例的吗？" + (horizontal == horizontal2 ? "是" : "否"));
        // 从文件中反序列化得到的 Orientation 对象单例吗？- 否
        System.out.println("反序列化得到的Orientation单例吗？" + (orientation1 == horizontal ? "是" : "否"));
        // 两次反序列化出来的对象是同一个吗？- 是
        System.out.println("两次反序列化出来的对象是同一个吗？" + (orientation1 == orientation3 ? "是" : "否"));
    }

    @Data
    private static class Animal implements Serializable{
        private String type;// 种类
        private Integer age;// 年龄

        public Animal(String type, Integer age) {
            this.type = type;
            this.age = age;
        }

        public Animal(String type) {
            this.type = type;
        }
    }

    // 探究当一个类的构造函数都是公有，且没有无参构造时的反序列化的行为
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

        Animal animal = new Animal("巨型暴龙", 12);

        oos.writeObject(animal);
        oos.close();
        System.out.println("=====================写入结束=====================");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Animal animal1 = (Animal) ois.readObject();
        System.out.println("Animal: " + animal1);
        ois.close();
    }

    // Orientation2(取向、方向类)，只可能取 HORIZONTAL(水平)、VERTICAL(垂直)两个值
    // 相较于 Orientation 类，新增覆盖了 readResolve()，用来保证反序列化单例以及类型
    // 安全的枚举时的单例性
    private static class Orientation2 implements Serializable {

        public static final Orientation2 HORIZONTAL = new Orientation2(1);// 水平方向
        public static final Orientation2 VERTICAL = new Orientation2(2);// 垂直方向

        private int value;

        private Orientation2(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Orientation2{" +
                    "value=" + value +
                    '}';
        }

        // 为了解决 test1 中带来的反序列化后得到的对象不是单例的问题
        private Object readResolve() throws InvalidObjectException {
            switch (value) {
                case 1:
                    return Orientation2.HORIZONTAL;
                case 2:
                    return Orientation2.VERTICAL;
                default:
                    throw new InvalidObjectException("对Orientation2无效的值");
            }
        }
    }

    @Test
    public void test3() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        Orientation2 horizontal = Orientation2.HORIZONTAL;
        Orientation2 vertical = Orientation2.VERTICAL;
        oos.writeObject(horizontal);
        oos.writeUTF("\n");
        oos.writeObject(vertical);
        oos.writeObject(horizontal);// 再写入一个 horizontal 对象
        oos.close();
        System.out.println("================写入结束================");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Orientation2 orientation1 = (Orientation2) ois.readObject();
        System.out.print(orientation1);
        String s = ois.readUTF();
        System.out.print(s);
        Orientation2 orientation2 = (Orientation2) ois.readObject();
        System.out.println(orientation2);
        Orientation2 orientation3 = (Orientation2) ois.readObject();
        System.out.println(orientation3);
        ois.close();
        // 经过测试，上面的读取和写入没有任何问题。
        System.out.println("=====================================");
        // 可以看到，上面的枚举类是单例的
        Orientation2 horizontal2 = Orientation2.HORIZONTAL;
        System.out.println("Orientation2是单例的吗？" + (horizontal == horizontal2 ? "是" : "否"));
        // 从文件中反序列化得到的 Orientation2 对象单例吗？- 是
        System.out.println("反序列化得到的Orientation2单例吗？" + (orientation1 == horizontal ? "是" : "否"));
        // 两次反序列化出来的对象是同一个吗？- 是
        System.out.println("两次反序列化出来的对象是同一个吗？" + (orientation1 == orientation3 ? "是" : "否"));
    }
}
