package com.mine.stream.entity;

import com.mine.stream.enums.StateEnum;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

import java.util.Map;

/**
 * @author CaoYang
 * @create 2023-04-09-2:40 PM
 * @description 每个实体的检查比较单元
 */
@Data
@Builder
@ToString
public class CheckUnit {

    private String key;
    // 差异映射
    private Map<String, FieldUnit> differentFields;
    // 对比状态
    private StateEnum state;

    @Tolerate
    public CheckUnit() {

    }
}
