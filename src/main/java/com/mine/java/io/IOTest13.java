package com.mine.java.io;

import lombok.Data;
import org.junit.Test;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CaoY
 * @date 2023-06-21 16:21
 * @description IO流学习 - 版本管理
 * 如下面的代码所示：Employee 类有两个版本，一个是没有字段 department 的，一个是有 department 字段的，
 * 操作的时候需要注释掉 department 相关内容。
 *
 * 下面的操作分别是：将没有 department 字段版本的 Employee 对象写入文件，再将 Employee 修改为有 department
 * 字段的版本将 Employee 对象读取出来。经过实验发现，如果没有下面的 serialVersionUID 字段，那么就会报
 * InvalidClassException 异常。反之操作也一样。而加上 serialVersionUID 则会正常读取，即如果被序列化（在文件中）
 * 的对象具有当前版本中所没有的数据域，那么对象输入流会忽略这些额外的数据；而如果当前版本具有在序列化的对象中所没有的
 * 数据域，那么这些新添加的域将被设置为它们的默认值（null、0、false）。
 *
 * 那么这个 serialVersionUID 该如何理解呢？
 * 1. serialVersionUID 是为了在序列化与反序列化时，正确匹配版本的类而引进的；
 * 2. 当一个可序列化的类被编译时，编译器会根据该类的结构自动计算一个默认的 serialVersionUID 值，这个值的计算方式
 * 通常基于类的结构，包括：字段、方法、继承关系等，当然我们也可以通过手动指定 serialVersionUID 覆盖默认的值，以确
 * 保序列化和反序列化的一致性。
 * 3. 在进行反序列化操作时，Java 会检查序列化数据中的 serialVersionUID 与目标类的 serialVersionUID 是否相同，
 * 如果相同说明序列化数据与目标类的版本一致（或是目标类的兼容序列化数据类的版本），可以成功序列化，如果不同，则会报：
 * InvalidClassException 异常，表示版本不一致（或不兼容），无法进行序列化操作。
 * 4. 因此 serialVersionUID 起到了保护序列化数据兼容性的作用，通过定义 serialVersionUID，我们可以确保在类的结构
 * 发生变化时仍然可以进行反序列化，同时也可以避免错误地反序列胡不同版本的类导致的数据不一致性或异常，但需要注意的是，一旦
 * 对类做了不可兼容的修改（如删除或修改字段、改变类的继承关系等），则必须更新 serialVersionUID，否则反序列化操作可能、
 * 会失败。
 */
public class IOTest13 {

    @Data
    public static class Employee implements Serializable {

        public static final long serialVersionUID = -3962364661247805043L;

        private String name;
        private Double salary;
        private LocalDate hireDay;
        private String department; // 部门作为 Employee 类的新版本的新增字段

        public Employee() {

        }

        public Employee(String name, Double salary, LocalDate hireDay) {
            this.name = name;
            this.salary = salary;
            this.hireDay = hireDay;
        }

        public Employee(String name, Double salary, LocalDate hireDay, String department) {
            this.name = name;
            this.salary = salary;
            this.hireDay = hireDay;
            this.department = department;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "name='" + name + '\'' +
                    ", salary=" + salary +
                    ", hireDay=" + hireDay +
                    ", department=" + department +
                    '}';
        }
    }

    @Test
    public void test1() {
        Employee employee1 = new Employee("张三", 12000.0, LocalDate.of(2023, 7, 15));
        Employee employee2 = new Employee("李四", 9000.0, LocalDate.of(2023, 8, 1));
        System.out.println(employee1);
        System.out.println(employee2);
        // serialVersionUID 可以手动指定，也可以通过下面方式自动生成：
        System.out.println("类Employee的serialVersionUID：" + ObjectStreamClass.lookup(Employee.class).getSerialVersionUID());
        System.out.println("由employee1推出的类Employee的serialVersionUID：" + ObjectStreamClass.lookup(employee1.getClass()).getSerialVersionUID());
        System.out.println("由employee2推出的类Employee的serialVersionUID：" + ObjectStreamClass.lookup(employee2.getClass()).getSerialVersionUID());
    }

    // 向 obj.dat 中写入多条 Employee 记录，注意这个版本的 Employee 没有 department 字段
    @Test
    public void test2() throws IOException, ClassNotFoundException {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("张三", 30000.0, LocalDate.of(2005, 7, 5)));
        list.add(new Employee("李四", 29000.0, LocalDate.of(2015, 3, 5)));
        list.add(new Employee("王五", 31000.0, LocalDate.of(2005, 2, 1)));
        list.add(new Employee("赵六", 45000.0, LocalDate.of(2002, 7, 12)));
        list.add(new Employee("候七", 32000.0, LocalDate.of(2005, 5, 8)));

//        list.add(new Employee("张三", 30000.0, LocalDate.of(2005, 7, 5), "销售部"));
//        list.add(new Employee("李四", 29000.0, LocalDate.of(2015, 3, 5), "采购部"));
//        list.add(new Employee("王五", 31000.0, LocalDate.of(2005, 2, 1), "业务部"));
//        list.add(new Employee("赵六", 45000.0, LocalDate.of(2002, 7, 12), "研发部"));
//        list.add(new Employee("候七", 32000.0, LocalDate.of(2005, 5, 8), "行政部"));

        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

        for (Employee employee : list) {
            oos.writeObject(employee);
        }

        oos.close();
        System.out.println("==================写入结束==================");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));

        List<Employee> res = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            res.add((Employee) ois.readObject());
        }
        res.forEach(System.out::println);

        ois.close();
    }

    @Test
    public void test3() throws IOException, ClassNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "files" + File.separator + "obj.dat";

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));

        List<Employee> res = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            res.add((Employee) ois.readObject());
        }
        res.forEach(System.out::println);

        ois.close();
    }
}
