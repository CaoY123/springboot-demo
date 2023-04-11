package com.mine.generic;

import com.fasterxml.classmate.GenericType;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author CaoY
 * @date 2023-04-11 23:43
 * @description 泛型测试6 - 获取泛型的类型
 * java.lang.reflect.Type 获取泛型
 */
public class GenericTest6<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static void main(String[] args) {

        GenericTest6<String> genericType = new GenericTest6<String>(){};
        Type superclass = genericType.getClass().getGenericSuperclass();
        //getActualTypeArguments 返回确切的泛型参数, 如Map<String, Integer>返回[String, Integer]
        Type type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
        System.out.println(type);//class java.lang.String
    }
}

