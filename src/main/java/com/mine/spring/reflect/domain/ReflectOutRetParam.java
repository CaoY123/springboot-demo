package com.mine.spring.reflect.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("反射出参")
public class ReflectOutRetParam {

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("编号")
    private Integer number;

    @ApiModelProperty("存款")
    private BigDecimal deposit;

    @ApiModelProperty("描述")
    private String desc;

    public ReflectOutRetParam(Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.number = (Integer) map.get("number");
        this.deposit = BigDecimal.valueOf(Double.parseDouble(map.get("deposit").toString()));
        this.desc = (String) map.get("desc");
    }
}
