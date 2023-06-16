package com.mine.java.io;

import com.mine.java.io.entity.User;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-06-15 14:24
 * @description IO流学习 - 以文本格式存储对象
 * 将 User 对象的属性存入到 users.txt 文件中。其中，使用 PrintWriter 将 User 数据进行写入，
 * 使用 Scanner 将写入的 User 输入从文件中读取出来，之后使用特定的分隔字符 | 对读出的 User 对象
 * 的属性进行解析，再生成新的 User 对象。
 */
public class IOTest4 {

    private static List<User> userList = null;
    static {
        userList = new ArrayList<>();
        userList.add(new User(1001, "张三", "男", 25, LocalDate.of(2000, 3, 12)));
        userList.add(new User(1002, "李四", "女", 22, LocalDate.of(2003, 4, 17)));
        userList.add(new User(1003, "王五", "女", 24, LocalDate.of(2001, 6, 30)));
        userList.add(new User(1004, "赵六", "男", 19, LocalDate.of(2006, 2, 25)));
        userList.add(new User(1005, "候七", "男", 31, LocalDate.of(1994, 7, 2)));
    }

    // 将 User 对象的属性按 | 间隔的方式写入到 users.txt 文件中
    @Test
    public void test1() throws FileNotFoundException, UnsupportedEncodingException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "users.txt";
        PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8.name());
        final String ATTR_SEPARATOR = "|";

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            pw.println(user.getId() + ATTR_SEPARATOR +
                    user.getName() + ATTR_SEPARATOR +
                    user.getGender() + ATTR_SEPARATOR +
                    user.getAge() + ATTR_SEPARATOR + user.getBirthday());
        }
        pw.close();
        System.out.println("===============================写入文件结束===============================");
    }

    // 从 users.txt 文件读取 test1 写入的 User 对象
    @Test
    public void test2() throws FileNotFoundException {
        String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator
                + "files" + File.separator + "users.txt";
        Scanner scanner = new Scanner(new File(path), StandardCharsets.UTF_8.name());
        final String ATTR_SEPARATOR = "|";

        List<User> users = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // 关于特殊字符，一定要加双斜杠进行转义，才能根据正确的符号进行分割
            String[] attrs = line.split("\\" + ATTR_SEPARATOR);
//            System.out.println(Arrays.toString(attrs));
            Integer id = Integer.parseInt(attrs[0]);
            String name = attrs[1];
            String gender = attrs[2];
            Integer age = Integer.parseInt(attrs[3]);
            LocalDate birthday = LocalDate.parse(attrs[4]);
            users.add(new User(id, name, gender, age, birthday));
        }
        // 要记得对流进行关闭
        if (scanner != null) {
            scanner.close();
        }
        // 读取 users.txt 放入 users 列表中遍历的结果为：
        users.forEach(System.out::println);
    }
}
