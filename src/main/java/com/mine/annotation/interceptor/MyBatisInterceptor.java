package com.mine.annotation.interceptor;

import com.mine.annotation.entity.LoginUser;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {
                MappedStatement.class, Object.class})})
public class MyBatisInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBatisInterceptor.class);

    private static final String CREATEUSER = "createUser";
    private static final String CREATEUSER_NAME = "createUserName";
    private static final String CREATEDATE = "createDate";
    private static final String UPDATEUSER = "updateUser";
    private static final String UPDATEUSER_NAME = "updateUserName";
    private static final String UPDATEDATE = "updateDate";
    private static final String INSTCODE = "instCode";
    private static final String VERSION = "version";

    private static final String USERID = "ADMIN";

    private static final String UPDATE_PREFIX = "update";


    private Map<String, Object> initInsertFieldMap = new HashMap<String, Object>();
    private Map<String, Object> initUpdateFieldMap = new HashMap<String, Object>();

    {
        initInsertFieldMap.put(CREATEUSER, USERID);
        initInsertFieldMap.put(CREATEUSER_NAME, USERID);
        initInsertFieldMap.put(UPDATEUSER, USERID);
        initInsertFieldMap.put(UPDATEUSER_NAME, USERID);
        initInsertFieldMap.put(CREATEDATE, null);
        initInsertFieldMap.put(UPDATEDATE, null);
        initInsertFieldMap.put(INSTCODE, (new Date()).getTime());
        initInsertFieldMap.put(VERSION, BigDecimal.ZERO);

        initUpdateFieldMap.put(UPDATEUSER, USERID);
        initUpdateFieldMap.put(UPDATEUSER_NAME, USERID);
        initUpdateFieldMap.put(UPDATEDATE, null);
        initUpdateFieldMap.put(INSTCODE, (new Date()).getTime());
    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        if (invocation.getArgs() == null || invocation.getArgs().length <= 1) {
            return invocation.proceed();
        }
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (sqlCommandType == null || SqlCommandType.DELETE == sqlCommandType) {
            return invocation.proceed();
        }


        Object parameter = null;
        if (invocation.getArgs()[1] instanceof MapperMethod.ParamMap){
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) invocation.getArgs()[1];
            if (paramMap == null || paramMap.size() == 0) {
                return invocation.proceed();
            }
            parameter = paramMap.containsKey("record") ? paramMap.get("record") : null;
        }else{
            parameter = invocation.getArgs()[1];
        }

        if (parameter == null){
            return invocation.proceed();
        }

        Field[] fields = parameter.getClass().getDeclaredFields();

        if (fields == null || fields.length == 0) {
            return invocation.proceed();
        }

        long timeCode = (new Date()).getTime();
        if (SqlCommandType.UPDATE == sqlCommandType) {
            LoginUser loginUser = new LoginUser("" + timeCode, "user-" + timeCode);
            initUpdateFieldMap.put(UPDATEUSER, loginUser.getUserAccount());
            initUpdateFieldMap.put(UPDATEUSER_NAME, loginUser.getUserName());
            for (Field field : fields) {
                initField(initUpdateFieldMap, field, parameter);
            }
        } else if (SqlCommandType.INSERT == sqlCommandType) {
            LoginUser loginUser = new LoginUser("" + timeCode, "user-" + timeCode);
            initInsertFieldMap.put(CREATEUSER, loginUser.getUserAccount());
            initInsertFieldMap.put(CREATEUSER_NAME, loginUser.getUserName());
            initInsertFieldMap.put(UPDATEUSER, loginUser.getUserAccount());
            initInsertFieldMap.put(UPDATEUSER_NAME, loginUser.getUserName());
            for (Field field : fields) {
                initField(initInsertFieldMap, field, parameter);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
    
    /**
     * 填充字段
     *
     * @param initFieldMap  保存：true
     * @param parameter    当前登录人id
     * @param field
     * @param parameter
     */
    private void initField(Map<String, Object> initFieldMap, Field field, Object parameter) {
        if(initFieldMap.keySet().contains(field.getName()) ) {
            Object fieldValue = (field.getName().toLowerCase().endsWith("date")
                    ? new Date() : initFieldMap.get(field.getName()));
            
            boolean accessible=field.isAccessible();
            field.setAccessible(true);
            try {
                Object obj = field.get(parameter);
                if (obj == null || (field.getName() != null && field.getName().startsWith(UPDATE_PREFIX))){
                    field.set(parameter, fieldValue);
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("set field value error");
            }
            field.setAccessible(accessible);
        }
    }



}