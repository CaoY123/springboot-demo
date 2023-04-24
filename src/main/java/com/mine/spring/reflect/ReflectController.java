package com.mine.spring.reflect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.domain.RetResult;
import com.mine.spring.exception.enums.RetCodeEnum;
import com.mine.spring.reflect.domain.ReflectOutRetParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;


import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CaoYang
 * @create 2023-04-24-11:44 AM
 * @description
 */
@RestController
@RequestMapping("/reflect")
public class ReflectController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public RetResult hello() {
        RetResult retResult = new RetResult();
        retResult.setCode(RetCodeEnum.SUCCESS);
        retResult.setMessage(RetCodeEnum.SUCCESS.getDesc());
        retResult.setResult("欢迎来到反射的世界");
        return retResult;
    }

    @GetMapping("/list")
    public RetResult list() {
        RetResult retResult = new RetResult();
        retResult.setCode(RetCodeEnum.SUCCESS);
        retResult.setMessage(RetCodeEnum.SUCCESS.getDesc());

        List<ReflectOutRetParam> list = new ArrayList<>();
        ReflectOutRetParam param1 = new ReflectOutRetParam("张三", 1, new BigDecimal(12.34), "张三的信息");
        ReflectOutRetParam param2 = new ReflectOutRetParam("李四", 2, new BigDecimal(13.56), "李四的信息");
        ReflectOutRetParam param3 = new ReflectOutRetParam("王五", 3, new BigDecimal(14.78), "王五的信息");
        ReflectOutRetParam param4 = new ReflectOutRetParam("赵六", 4, new BigDecimal(15.99), "赵六的信息");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);

        retResult.setResult(list);
        return retResult;
    }

    @GetMapping("/list/change")
    public RetResult change() throws JsonProcessingException {
        RetResult<List<ReflectOutRetParam> > retResult = new RetResult();
        retResult.setCode(RetCodeEnum.SUCCESS);
        retResult.setMessage(RetCodeEnum.SUCCESS.getDesc());

        List<ReflectOutRetParam> list = new ArrayList<>();
        ReflectOutRetParam param1 = new ReflectOutRetParam("张三", 1, new BigDecimal(12.34), "张三的信息");
        ReflectOutRetParam param2 = new ReflectOutRetParam("李四", 2, new BigDecimal(13.56), "李四的信息");
        ReflectOutRetParam param3 = new ReflectOutRetParam("王五", 3, new BigDecimal(14.78), "王五的信息");
        ReflectOutRetParam param4 = new ReflectOutRetParam("赵六", 4, new BigDecimal(15.99), "赵六的信息");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);

        retResult.setResult(list);

        String str = objectMapper.writeValueAsString(retResult);
        System.out.println(str);

        RetResult<Object> objectRetResult = objectMapper.readValue(str, new TypeReference<RetResult<Object>>() {
            @Override
            public Type getType() {
                return ParameterizedTypeImpl.make(RetResult.class, new Type[]{Object.class}, null);
            }
        });

//        RetResult retResult1 = objectMapper.readValue(str, RetResult.class);
        RetResult<List<ReflectOutRetParam> > retResult1 = convertResult(objectRetResult);

        return retResult1;
    }

    @GetMapping("/list/change2")
    public RetResult<List<ReflectOutRetParam> > change2() throws JsonProcessingException {
        RetResult<List<ReflectOutRetParam> > retResult = new RetResult();
        retResult.setCode(RetCodeEnum.SUCCESS);
        retResult.setMessage(RetCodeEnum.SUCCESS.getDesc());

        List<ReflectOutRetParam> list = new ArrayList<>();
        ReflectOutRetParam param1 = new ReflectOutRetParam("张三", 1, new BigDecimal(12.34), "张三的信息");
        ReflectOutRetParam param2 = new ReflectOutRetParam("李四", 2, new BigDecimal(13.56), "李四的信息");
        ReflectOutRetParam param3 = new ReflectOutRetParam("王五", 3, new BigDecimal(14.78), "王五的信息");
        ReflectOutRetParam param4 = new ReflectOutRetParam("赵六", 4, new BigDecimal(15.99), "赵六的信息");
        list.add(param1);
        list.add(param2);
        list.add(param3);
        list.add(param4);

        retResult.setResult(list);

        String jsonStr = objectMapper.writeValueAsString(retResult);
        System.out.println(jsonStr);

        TypeReference<RetResult<List<ReflectOutRetParam> > > typeReference = new TypeReference<RetResult<List<ReflectOutRetParam>>>() {};
        RetResult<List<ReflectOutRetParam>> result = objectMapper.readValue(jsonStr, typeReference);

        return result;
    }

    private RetResult<List<ReflectOutRetParam> > convertResult(RetResult<Object> objectRetResult) {
        Object objResult = objectRetResult.getResult();
        List<?> objResult1 = (List<?>) objResult;
        List<ReflectOutRetParam> result = objResult1.stream()
                .map(item -> {
                    if (!(item instanceof LinkedHashMap<?, ?>)) {
                        throw new IllegalArgumentException("The list item is not an instance of LinkedHashMap.");
                    }
                    LinkedHashMap<String, Object> itemMap = (LinkedHashMap<String, Object>) item;
                    return new ReflectOutRetParam(itemMap);}
                )
                .collect(Collectors.toList());

        RetResult<List<ReflectOutRetParam> > res = new RetResult<>();
        res.setCode(objectRetResult.getCode());
        res.setMessage(objectRetResult.getMessage());
        res.setResult(result);
        return res;
    }
}
