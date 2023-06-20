package com.mine.java.io;

import cn.hutool.core.codec.Base64;
import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.junit.Test;

import java.io.*;

/**
 * @author CaoY
 * @date 2023-06-20 13:00
 * @description IO流学习 - 序列化机制学习（writeObject 和 readObject）
 * 首先介绍了 transient（瞬态），即不参与（或不直接参与）序列化的字段。下面的 Person 类
 * 中的两个实例字段 name 和 age 以及 一个静态字段 type 都是默认参与实例化的，而另一个被
 * transient 修饰的实例字段 password 则不参与序列化，因为密码信息涉及秘密，序列化一般用
 * 于信息的传输，因此，需要加密后传输，而不能直接将原密码传输。下面的 test1和test2的两个练习
 * 也展示了这一过程。
 * 其中，重点关注 Person 类，其实现了 Serializable 接口，且覆盖了 writeObject 和 readObject。
 * 自定义的 writeObject 会在序列化时被自动通过反射调用，而自定义的 readObject 则会在反序列化时
 * 也自动通过反射调用。而且这里一般会将其定义为 private 权限的，是为了防止子类和其他类直接调用该方法，
 * 避免破坏序列化的一致性和安全性。
 *
 * defaultWriteObject() 和 defaultReadObject()
 * 1. defaultWriteObject()：写入非 transient 字段；
 * 2. defaultReadObject()：读取写入的非 transient 字段值并将它们设置到相应的字段上。
 * * 注意，对于 transient 字段，需要单独处理，遵循“怎么写的就怎么读出来”的原则
 */
public class IOTest10 {

    @Data
    private static class Person implements Serializable {
        private String name;
        private Integer age;
        private transient String password;// 个人的密码，不作为直接序列化的事物，需要加密后写入
        private static String type;// 类型，默认为“人类”

        static {
            type = "人类";
        }

        public Person() {
            this.password = "123456";
        }

        public Person(String name, Integer age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        private void writeObject(@NotNull ObjectOutputStream oos) throws IOException {
            oos.defaultWriteObject();
            // 密码使用 Base64加密 后再输出
            String encodeStr = Base64.encode(password);
            oos.writeUTF(encodeStr);
        }

        private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
            ois.defaultReadObject();
            String passwordStr = ois.readUTF();
            System.out.println("加密后的密码：" + passwordStr);
            byte[] decode = Base64.decode(passwordStr);
            this.password = new String(decode);
            System.out.println("原密码：" + this.password);
        }
    }

    // 将一个 Person 对象写入到 obj.dat 中
    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

        Person person = new Person("张三", 23, "12345678");

        oos.writeObject(person);

        oos.close();
        System.out.println("=======================写入结束=======================");
    }

    // 将test1中写入到 obj.dat 的 Person 对象读取出来
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));


        Person person = (Person) ois.readObject();
        System.out.println("**************************");
        System.out.println(person);
        ois.close();
    }
}
