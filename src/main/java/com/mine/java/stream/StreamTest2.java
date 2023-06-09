package com.mine.java.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author CaoY
 * @date 2023-06-09 12:51
 * @description 流的处理
 * 对于 stream 的 filter、map 和 flatMap的比较：
 * 上述三者操作后均产生一个流；
 * filter产生的流中包含满足谓词条件的元素；（过滤）
 * map产生的流式将传入其中的方法应用于流中所有元素而产生的结果；（映射）
 * flatmap 类似于 map，但是它在map的基础上还会将其中每个流产生的结果再连接到一起而获得一个新的流
 */
public class StreamTest2 {

    @Test
    public void test1() {
        // map，按照某种方式对流中的值进行映射
        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
        String[] splitWord = words.split(",");
        // 将单词字母转换为大写
        Iterator<String> iterator = Stream.of(splitWord).map(String::toUpperCase).iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("****************************************************");

        // 取出每个单词中的首字母并遍历打印
        Stream.of(splitWord).map(s -> s.substring(0, 1)).forEach(System.out::println);

        System.out.println("================================End of the procedure================================");
    }

    @Test
    public void test2() {
        // 调用codePoints()
//        codePoints("Power").forEach(System.out::println);

        String words = "springboot,demo,domain,algorithm,enums,listen,practice,stream,python,utils";
        String[] splitWord = words.split(",");

        // 将codePoints()方法映射到一个字符串流上，使用flatMap将codePoints()调用的结果摊平成单个流
        Stream.of(splitWord).flatMap(s -> codePoints(s)).forEach(System.out::println);
    }

    public static Stream<String> codePoints(String s) {
        List<String> list = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            int j = s.offsetByCodePoints(i, 1);
            list.add(s.substring(i, j));
            i = j;
        }
        return list.stream();
    }
}
