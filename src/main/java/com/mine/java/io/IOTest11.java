package com.mine.java.io;

import lombok.Data;
import org.junit.Test;

import java.io.*;

/**
 * @author CaoY
 * @date 2023-06-20 16:36
 * @description IO流学习 - 序列化机制学习（Externalizable）
 * 下面的练习主要聚焦 Person 类，实现了 Externalizable 接口，重写了 writeExternal 和 readExternal方法，
 * 可以看到，Externalizable 接口并不提供默认的行为，而是需要自己逐个写入内容。因此，相比于 Serializable ，
 * 具有更细粒度的读写控制，而 Serializable 则是提供了一种默认的读取和写入的行为，提供了相对粗粒度的控制，在对类
 * 不清楚的情况下可以使用。
 */
public class IOTest11 {

    @Data
    private static class Person implements Externalizable {

        private String name;
        private Integer age;

        public Person() {

        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeUTF(name);
            out.writeInt(age);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            this.name = in.readUTF();
            this.age = in.readInt();
        }
    }

    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

        Person person = new Person("李四", 19);

        oos.writeObject(person);

        oos.close();
        System.out.println("========================写入结束========================");
    }

    @Test
    public void test2() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));

        Person person = (Person) ois.readObject();
        System.out.println(person);

        ois.close();
    }
}
