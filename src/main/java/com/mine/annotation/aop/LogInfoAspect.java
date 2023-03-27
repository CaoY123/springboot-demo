package com.mine.annotation.aop;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mine.annotation.anno.LogInfo;
import com.mine.annotation.entity.AccountLogBaseInfo;
import com.mine.annotation.manager.AccountLogBaseInfoManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.Objects;

/**
 * @author CaoYang
 * @create 2023-03-16-3:41 PM
 * @description 公共操作日志收集注解 切面解析类
 */
@Component
@Aspect
public class LogInfoAspect {

    @Autowired
    private AccountLogBaseInfoManager accountLogBaseInfoManager;

    @Pointcut("@annotation(com.mine.annotation.anno.LogInfo)")
    public void pointcut() {}

    /**
     * 后置通知的形式记录日志，因为可能涉及到获取响应体数据
     * @param joinPoint 切入点对象
     */
    @Around("pointcut() && @annotation(loggerInfo)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, LogInfo loggerInfo) {
        // 用于封装必要信息
        AccountLogBaseInfo logBaseInfo = new AccountLogBaseInfo();

        // 设置id
        logBaseInfo.setId(UUID.fastUUID().toString());

        // 获取签名信息
        Signature signature = joinPoint.getSignature();
        // 获取方法的参数值数组
        Object[] args = joinPoint.getArgs();

        // 获取执行的结果
        Object result = null;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable e) {
            e.printStackTrace();
            if (loggerInfo.showExp()) {
                logBaseInfo.setExceptionInfo(e.getMessage());
            }
        }

        // 获取使用注解的 Method 对象
        Method method = ((MethodSignature)signature).getMethod();
        if (method != null) {
            // 获取使用注解的方法的 参数对象 func(accountId)
            Parameter[] parameters = method.getParameters();
            // 在其中寻找名字
            int index = 0;
            boolean flag = false;
            for (Parameter parameter : parameters) {
                if (parameter.getName().equals(loggerInfo.accountIdName())) {
                    logBaseInfo.setAccountId((String) args[index]);
                    flag = true;
                    break;
                }
                index++;
            }

            // accountId 还没有设置
            if (!flag) {
                index = 0;
                // 可能在该方法的参数列表中没有叫 accountId 的参数名
                for (Parameter parameter : parameters) {
                    String accountIdStr = traversalToFind(args[index], parameter.getType(), loggerInfo.accountIdName());
                    if (Objects.nonNull(accountIdStr)) {
                        logBaseInfo.setAccountId(accountIdStr);
                        flag = true;
                        break;
                    }
                    index++;
                }
            }

            if (!flag) {
                // todo 如果 accountId 不存在，需不需要做一些处理？
                try {
                    throw new RuntimeException("使用注解的方法中未找到与账户号有关的配置");
                } finally {
                    return result;
                }
            }

            // 设置模块名
            logBaseInfo.setModuleName(loggerInfo.moduleName().getModuleMsg());

            // 设置操作类型
            logBaseInfo.setOperateType(loggerInfo.operateType().getOperateType());

            // 设置操作用户和操作时间
//            logBaseInfo.setCreateUser("test1_c");
//            logBaseInfo.setUpdateUser("test1_u");

            logBaseInfo.setCreateDate(new Date());
            logBaseInfo.setUpdateDate(new Date());

            if (loggerInfo.showReq()) {
                // 操作人、操作时间通过洛库时由MyBatis的拦截器设置
                ObjectMapper objectMapper = new ObjectMapper();

                ObjectNode rootNode = objectMapper.createObjectNode();

                index = 0;
                for (Parameter parameter : parameters) {
                    // 定义一个 JsonNode
                    JsonNode jsonNode = objectMapper.valueToTree(args[index]);
                    rootNode.set(parameter.getName(), jsonNode);
                    index++;
                }
                logBaseInfo.setRequestParams(rootNode.toString());
            }

            if (loggerInfo.showResp() && Objects.nonNull(result)) {
                // 记录响应参数
                String respJsonStr = JSONUtil.toJsonStr(result);
                logBaseInfo.setResponseParams(respJsonStr);
            }

            accountLogBaseInfoManager.save(logBaseInfo);

            return result;

        } else {
            try {
                throw new RuntimeException("使用注解的方法不未找到");
            } finally {
                return result;
            }
        }
    }

    /**
     * 单层遍历找字段的 String 描述的值，这里只查找一层，是希望使用者对于 accountId 的封装不要太深
     * @param obj       取值的对象
     * @param clazz     obj的实际的类型的Class对象
     * @param name      要获取的字段名
     * @param <T>       obj的泛型类型
     * @return          以字符串形式返回 字段名为 name 的值
     */
    private <T> String traversalToFind(Object obj, Class<T> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        T tObj = clazz.cast(obj);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                System.out.println("字段名：" + field.getName());
                Method method = null;
                String res = null;
                try {
                    method = clazz.getDeclaredMethod("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1));
                    res = method.invoke(tObj) + "";
                } catch (Exception e) {
                    throw new RuntimeException("get方法出错");
                }
                return res;
            }
        }
        return null;
    }

}
