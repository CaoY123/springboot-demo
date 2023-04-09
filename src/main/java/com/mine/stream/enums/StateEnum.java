package com.mine.stream.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author CaoYang
 * @create 2023-04-09-1:51 PM
 * @description 状态类型枚举值
 */
@Getter
@AllArgsConstructor
public enum StateEnum {

    WAITED_CHECK("waited_check", "待比对"),
    SOURCE_MORE("source_more", "源多数据"),
    TARGET_MORE("target_more", "目标多数据"),
    DIFFERENT("different", "存在差异");

    private String val;
    private String desc;

    public static StateEnum findByVal(String val) {
        for (StateEnum stateEnum : StateEnum.values()) {
            if (stateEnum.getVal().equals(val)) {
                return stateEnum;
            }
        }
        throw new RuntimeException("存在非法的状态类型【" + val + "】" );
    }
}
