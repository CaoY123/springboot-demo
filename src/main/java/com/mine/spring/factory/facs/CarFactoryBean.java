package com.mine.spring.factory.facs;

import com.mine.spring.factory.domain.Car;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author CaoYang
 * @create 2023-04-22-10:31 AM
 * @description 用于测试SmartFactoryBean<T> 的作用
 * SmartFactoryBean的 isPrototype() 和 isEagerInit()，主要向Spring框架提供额外的元信息以优化内部行为，
 * 但并不会直接控制工厂bean的实际行为，如果不自己进行一些处理的话，它是不会有实际效果的
 * 个人小结：貌似Spring没有提供一种足够方便的可以通过返回true或false来自动开启或关闭单例的机制，而是需要我么
 * 自己去添加额外的东西去保证单例或多例，但是由于我目前知识水平有限，所以这个论断也不一定正确，后面还会再回来审视
 */
@Component
public class CarFactoryBean implements SmartFactoryBean<Car> {

    private String brand;

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public Car getObject() throws Exception {
        return new Car(brand);
    }

    @Override
    public Class<?> getObjectType() {
        return Car.class;
    }

    @Override
    public boolean isSingleton() {
        return SmartFactoryBean.super.isSingleton();
    }

    // 注：
    @Override
    public boolean isPrototype() {
        return true;
//        return false;
    }

    @Override
    public boolean isEagerInit() {
        return false;
    }
}
