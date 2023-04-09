package com.mine.stream;



import cn.hutool.core.util.ObjectUtil;
import com.mine.stream.entity.CheckUnit;
import com.mine.stream.entity.FieldUnit;
import com.mine.stream.entity.MyEntry;
import com.mine.stream.enums.StateEnum;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author CaoYang
 * @create 2023-04-07-6:28 PM
 * @description 对于两个Map中的元素，运用stream操进行比对
 * 找出下面构造的 sourceMap 与 targetMap的不同，包括：
 * sourceMap中存在的key且在targetMap中不存在的key的记录；
 * targetMap中存在的key但在sourceMap中不存在的key的记录；
 * 两个集合都存在的key但是其值映射（dataMap）不一样的记录，都要把它们记录下来
 */
public class CompareMapTest {
    public static void main(String[] args) {
        init();
        // 进行两个Map对比的复现
        List<CheckUnit> unitDetails = new ArrayList<>();
        sourceMap.forEach((k, v) -> {
            if (!targetMap.containsKey(k)) {
                unitDetails.add(CheckUnit.builder()
                        .key(k)
                        .state(StateEnum.SOURCE_MORE)
                        .differentFields(createSingle(v.getDataMap(), true))
                        .build());
                return;
            }
            // 比较key相同的dataMap的不同
            Map<String, Object> sourceDataMap = v.getDataMap();
            Map<String, Object> targetDataMap = targetMap.get(k).getDataMap();

            Map<String, FieldUnit> differentMap = compareBothCheckData(sourceDataMap, targetDataMap);
            if (differentMap == null || differentMap.isEmpty()) {
                return;
            }

            unitDetails.add(CheckUnit.builder()
                            .key(k)
                            .state(StateEnum.DIFFERENT)
                            .differentFields(differentMap)
                    .build());
        });

        targetMap.forEach((k, v) -> {
            if (!sourceMap.containsKey(k)) {
                unitDetails.add(CheckUnit.builder()
                                .key(k)
                                .state(StateEnum.TARGET_MORE)
                                .differentFields(createSingle(v.getDataMap(), false))
                        .build());
            }
        });

        // 展示一下unitDetails：
        for (CheckUnit unitDetail : unitDetails) {
            System.out.println("***************************");
            System.out.println("key -> " + unitDetail.getKey());
            System.out.println("state -> " + unitDetail.getState());
            Map<String, FieldUnit> differentFields = unitDetail.getDifferentFields();
            System.out.println("========different fields=======");
            if (differentFields != null || !differentFields.isEmpty()) {
                for (Map.Entry<String, FieldUnit> entry : differentFields.entrySet()) {
                    System.out.println(entry.getKey() + " -> " + entry.getValue());
                }
            }
            System.out.println("=================================");
            System.out.println("***************************");
        }
    }

    private static Map<String, MyEntry> sourceMap;
    private static Map<String, MyEntry> targetMap;

    // 初始化两个Map
    // 添加元素的规则：偶数位序加在sourceMap，奇数位序加在targetMap，为3的倍数时都会往两个集合中加进去，而对于两个Map中存在的
    // 公共部分，选择其中一个（下标为6的Map，是的其中的MyEntry对象里的dataMap集合含有不同的内容）
    private static  void init() {
        Random random = new Random(System.currentTimeMillis());
        List<MyEntry> sourceEntryList = new ArrayList<>();
        List<MyEntry> targetEntryList = new ArrayList<>();

        // 自定义排序规则
        Comparator<MyEntry> comparator = new Comparator<MyEntry>() {
            @Override
            public int compare(MyEntry o1, MyEntry o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        };

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

            if (i == 6) {
                Map<String, Object> map2 = ObjectUtil.clone(map);
                map2.put("key-aakk", "格外加入的值");

                MyEntry myEntry1 = MyEntry.builder()
                        .key(key)
                        .entry(entry)
                        .dataMap(map)
                        .build();
                sourceEntryList.add(myEntry1);

                MyEntry myEntry2 = MyEntry.builder()
                        .key(key)
                        .entry(entry)
                        .dataMap(map2)
                        .build();
                targetEntryList.add(myEntry2);
                continue;
            }

            MyEntry myEntry = MyEntry.builder()
                    .key(key)
                    .entry(entry)
                    .dataMap(map)
                    .build();

//            System.out.println(myEntry);
            if (i % 2 == 0) {
                sourceEntryList.add(myEntry);
            } else {
                targetEntryList.add(myEntry);
            }
            if (i % 3 == 0 && i % 2 == 1) {
                sourceEntryList.add(myEntry);
            }
            if (i % 3 == 0 && i % 2 == 0) {
                targetEntryList.add(myEntry);
            }
        }

        // 用stream操作转换为一个Map
        sourceMap = sourceEntryList.stream().collect(Collectors.toMap(MyEntry::getKey, Function.identity()));
        targetMap = targetEntryList.stream().collect(Collectors.toMap(MyEntry::getKey, Function.identity()));
//
//        for (Map.Entry<String, MyEntry> entry : sourceMap.entrySet()) {
//            System.out.println(entry.getKey() + " -> " + entry.getValue());
//        }
//        System.out.println("***********************************");
//        for (Map.Entry<String, MyEntry> entry : targetMap.entrySet()) {
//            System.out.println(entry.getKey() + " -> " + entry.getValue());
//        }
    }

    /**
     * 当为source_more 或 target_more时，将checkData转换为fieldUnitMap
     * @param checkData 原始值
     * @param isSource 数据来源：true：源，false：目标
     * @return
     */
    public static Map<String, FieldUnit> createSingle(Map<String, Object> checkData, boolean isSource) {
        Map<String, FieldUnit> different = new HashMap<>();
        checkData.forEach((k, v) -> {
            if (isSource) {
                different.put(k, FieldUnit.builder()
                                .fieldName(k)
                                .source(v)
                        .build());
            } else {
                different.put(k, FieldUnit.builder()
                                .fieldName(k)
                                .target(v)
                        .build());
            }
        });
        return different;
    }

    // 在前面key一样的情况下对比dataMap的不同
    public static Map<String, FieldUnit> compareBothCheckData(Map<String, Object> sourceCheckData, Map<String, Object> targetCheckData) {
        Map<String, FieldUnit> different = new HashMap<>();
        sourceCheckData.forEach((k, v) -> {
            if (targetCheckData.containsKey(k) && v.equals(targetCheckData.get(k))) {
                return;
            }
            different.put(k, FieldUnit.builder()
                            .fieldName(k)
                            .source(v)
                            .target(targetCheckData.get(k))
                    .build());
        });
        targetCheckData.forEach((k, v) -> {
            if (sourceCheckData.containsKey(k)) {
                return;
            }
            different.put(k, FieldUnit.builder()
                            .fieldName(k)
                            .target(v)
                    .build());
        });
        return different;
    }

    // 测试hutool的深拷贝方法
    @Test
    public void test1() {
        init();
        Map<String, MyEntry> map = ObjectUtil.clone(sourceMap);
        System.out.println("map.size = " + map.size());
        System.out.println(map == sourceMap);
    }
}
