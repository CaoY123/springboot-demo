package com.mine.annotation.entity;

/**
 * @author CaoYang
 * @create 2023-03-20-6:46 PM
 * @description 操作类型枚举
 */
public enum OperateEnum {

    INSERT("insert", "插入"),
    UPDATE("update", "更新"),
    DELETE("delete", "删除"),
    SELECT("select", "查询");

    private String operateName;
    private String operateType;

    OperateEnum(String operateName, String operateType) {
        this.operateName = operateName;
        this.operateType = operateType;
    }

    public String getOperateName() {
        return operateName;
    }

    public String getOperateType() {
        return operateType;
    }
}
