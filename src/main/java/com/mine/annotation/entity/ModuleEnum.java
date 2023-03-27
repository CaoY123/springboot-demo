package com.mine.annotation.entity;

/**
 * @author CaoYang
 * @create 2023-03-20-6:38 PM
 * @description
 */
public enum ModuleEnum {

    DEDUCT("DEDUCT", "扣款"),
    ENTER_INTO_ACCOUNT("ENTER_INTO_ACCOUNT", "入账"),
    RERUN("RERUN", "重跑");


    private String moduleName;
    private String moduleMsg;

    ModuleEnum(String moduleName, String moduleMsg) {
        this.moduleName = moduleName;
        this.moduleMsg = moduleMsg;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getModuleMsg() {
        return moduleMsg;
    }
}
