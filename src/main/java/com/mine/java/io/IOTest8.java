package com.mine.java.io;

import com.mine.java.io.entity.Employee;
import com.mine.java.io.entity.Manager;
import com.mine.java.io.entity.User;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-06-17 16:33
 * @description IO流学习 - 输入输出流的序列化
 * 主要是练习 ObjectOutputStream 和 ObjectInputStream 的使用。
 * 注意：下面的代码为了清晰性，关闭流的操作上是很粗糙的，具体的应该将其放在 try-with-resources 语句块中，
 * 确保流可以安全的关闭，有什么异常也能捕获及时处理。
 *
 * 对象的序列化会产生一个序列号，用于标记写入的对象。可以用于识别反序列化后是否是同一个对象，而且序列化与内存
 * 无关，可以用于将对象通过网络传输。
 */
public class IOTest8 {

    private static List<Employee> employeeList = null;
    static {
        employeeList = new ArrayList<>();
        Employee secretary = new Employee(1035, "小明", "A集团", "秘书");
        employeeList.add(secretary);
        // 两个经理有同一个秘书
        employeeList.add(new Manager(1006, "小强", "A集团", "经理",
                secretary, "采购部"));
        employeeList.add(new Manager(1007, "小红", "A集团", "经理",
                secretary, "营销部"));
    }

    // 将对象 User 写入文件 obj.dat
    @Test
    public void test1() throws IOException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

        User user = new User(1001, "张三", "男", 23, LocalDate.of(2000, 4, 29));
        oos.writeObject(user);
        oos.close();
        System.out.println("=======================写入对象结束=======================");
    }

    // 将 test1 中写入到 obj.dat 中的对象 User 读取出来
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));

        User user = (User) ois.readObject();
        System.out.println(user);
        ois.close();
    }

    /**
     * 下面描述的是当一个对象被多个对象共享时，且这个被共享的对象作为它们各自状态的一部分时
     * 如下面的案例，两个 Manager 都有一个共同的 secretary(秘书)，这样的对象在写入文件后，
     * 再读取的时候要保证引用对象的一致性，java中对于这个问题的解决是通过一个序列号来解决的，而不是
     * 记录对象的内存地址（因为数据通过网络传输后内存地址会不一致），但是一个唯一的序列号可以保证对一个对象的共享。
     * 这就是为什么要叫做序列化的原因。下面的练习是将两个拥有相同秘书的 Manager 和那位秘书对象写入文件，之后，再从
     * 文件中读取出来，再比较读取出来的两个 Manager 的秘书是否是同一个，以及两个 Manager 的秘书是否和读取出来的
     * 秘书对象是同一个。
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void test3() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

        for (Employee employee : employeeList) {
            oos.writeObject(employee);
        }

        oos.close();
        System.out.println("===================================");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        Manager[] managers = new Manager[2];
        Employee secretary = null;
        int index = 0;
        for (int i = 0; i < 3; i++) {
            Employee employee = (Employee) ois.readObject();
            if (employee instanceof Manager) {
                managers[index++] = (Manager)employee;
            } else {
                secretary = employee;
            }
        }
//        System.out.println(Arrays.toString(managers));
//        System.out.println(secretary);
        Employee sec1 = managers[0].getSecretary();
        Employee sec2 = managers[1].getSecretary();
        System.out.println("两位经理的秘书是否是同一位：" + (sec1 == sec2 ? "是" : "否"));
        System.out.println("秘书是否就是那位写入到文件中的员工：" + (sec1 == secretary ? "是" : "否"));
    }
}
