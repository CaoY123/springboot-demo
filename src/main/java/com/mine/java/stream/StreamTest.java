package com.mine.java.stream;

import com.mine.java.stream.entity.MyEntry;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CaoYang
 * @create 2023-04-07-5:46 PM
 * @description 关于java中stream的操作测试
 *
 */
public class StreamTest {

    @Test
    public void test1() {
        Random random = new Random(System.currentTimeMillis());
        List<MyEntry> myEntryList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String key = "key-" + i;
            String entry = "entry-" + i;
            Map<String, Object> map = new HashMap<>();
            int randomMapSize = (int) (Math.random() * (11 - 2));
            for (int j = 0; j < randomMapSize; j++) {
                String ky = key + "-" + (random.nextInt(899) + 100);
                int val = random.nextInt(29) + 1;
                map.put(ky, val);
            }

            MyEntry myEntry = MyEntry.builder()
                    .key(key)
                    .entry(entry)
                    .dataMap(map)
                    .build();

//            System.out.println(myEntry);
            myEntryList.add(myEntry);
        }

        // 用stream操作转换为一个List
        Map<String, MyEntry> resultMap = myEntryList.stream().collect(Collectors.toMap(MyEntry::getKey, Function.identity()));
        for (Map.Entry<String, MyEntry> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }


}
