package com.mine.java.stream;

import com.mine.java.stream.entity.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-10 10:15
 * @description Optional类的其他操作
 * 展示了flatMap用于组装Optional值相关的流程，但是对于flatMap的理解自认为还没有到位；
 * Optional的 map 和 flatMap 都是对Optional包装的对象进行映射操作，都是往其中传入相应的方法，但是与map不同的是，
 * flatMap期望传入一个返回值是Optional类型的方法
 */
public class OptionalTest2 {

    @Test
    public void test1() {
        // 计算某个数字倒数的平方根
        double x = 0.5;
        // flatMap用于链式的组装两个计算步骤，flatMap将Optional的值以此应用在传入的方法中
        Optional<Double> result = inverse(x).flatMap(OptionalTest2::squareRoot);
        System.out.println(result);
    }

    // 计算倒数
    public static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1.0 / x);
    }

    // 计算平方根
    public static Optional<Double> squareRoot(Double x) {
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }

    @Test
    public void test2() {
//        Optional<User> opt1 = lookup(null); // 测试给lookup传入null值方法的返回值，结果为空的Optional对象，结果正确
        Optional<User> opt1 = lookup(5);
        System.out.println(opt1);

        // id集合，有的id是userLists有的，称为有效id；而userList里没有的则是无效id
        List<Integer> ids = Arrays.asList(7, 1, 4, 8, 9, 10 ,2, 6, 5, 31, 3, 12);



        // 使用Optional的 isPresent 和 get 方法来获取查询后的有效的值。
//        ids.stream().map(OptionalTest2::lookup).filter(Optional::isPresent).map(Optional::get).forEach(System.out::println);
        // 没有过滤的效果在里面
        ids.stream().map(OptionalTest2::lookup).forEach(System.out::println);
    }

    private static List<User> userLists = null;
    static {
        userLists = new ArrayList<>();
        userLists.add(new User(1, "张三", "男", 23));
        userLists.add(new User(2, "李四", "女", 27));
        userLists.add(new User(3, "王五", "女", 32));
        userLists.add(new User(4, "赵六", "男", 16));
        userLists.add(new User(5, "候七", "男", 19));
    }

    public static Optional<User> lookup(Integer id) {
        Optional<User> result = userLists
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        return result;
    }
}
