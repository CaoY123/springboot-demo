package com.mine.annotation.anno;

import com.mine.annotation.entity.ModuleEnum;
import com.mine.annotation.entity.OperateEnum;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

/**
 * @author CaoYang
 * @create 2023-03-16-3:34 PM
 * @description 公共操作日志收集注解 收集信息如下：
 * 账户号	    account_id
 * 模块	        扣款、入账、重跑等
 * 操作类型       如更新、删除、插入等
 * 操作人        工号+姓名
 * 操作时间       yyyy-mm-dd hh:mm:ss.sss
 * 请求参数       可根据日志注解增加是否记录请求参数的属性
 * 返回参数       可根据日志注解增加是否记录请求参数的属性
 * 异常信息       可根据日志注解增加是否记录异常信息的属性
 * 关联id        记录操作记录的唯一记录id
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogInfo {

    /**
     * 账户号对应的变量名，默认为accountId，可以另外作指定
     *
     */
    @NotNull
    String accountIdName() default "accountId";

    /**
     * 模块名，如果指定了就按照这个取值，如果不指定则按照默认的一套规则来指定
     * 必须传递 ModuleEnum 枚举中的常量
     */
    @NotNull
    ModuleEnum moduleName();

    /**
     * 操作类型：插入、更新、删除等
     * 必须传递 OperateEnum 中的常量
     */
    @NotNull
    OperateEnum operateType();

    /**
     * 是否记录请求参数，为 true 时记录
     */
    boolean showReq() default true;

    /**
     * 是否记录响应参数，为 true 时记录
     */
    boolean showResp() default true;

    /**
     * 是否记录异常参数，为 true 时记录
     */
    boolean showExp() default true;

}
