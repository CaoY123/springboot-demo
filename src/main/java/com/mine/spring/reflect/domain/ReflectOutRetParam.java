package com.mine.spring.reflect.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author CaoYang
 * @create 2023-04-24-11:49 AM
 * @description 反射示例出惨
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReflectOutRetParam {
    private String name;
    private Integer number;
    private BigDecimal deposit;
    private String desc;

    public ReflectOutRetParam(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.number = (Integer) map.get("number");
        this.deposit = BigDecimal.valueOf(Double.parseDouble(map.get("deposit").toString()));
        this.desc = (String) map.get("desc");
    }
}
