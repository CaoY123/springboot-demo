package com.mine.spring.factory;

import com.mine.spring.factory.domain.Car;
import com.mine.spring.factory.facs.CarFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author CaoYang
 * @create 2023-04-22-10:35 AM
 * @description 关于Spring工厂类的测试，用于测试SmartFactoryBean<T> 的作用
 */
@SpringBootApplication
public class SpringBootFactoryDemoApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootFactoryDemoApplication.class, args);
        CarFactoryBean factoryBean = applicationContext.getBean(CarFactoryBean.class);

        Car car1 = factoryBean.getObject();
//        Car car2 = factoryBean.getObject();

        CarFactoryBean factoryBean2 = applicationContext.getBean(CarFactoryBean.class);
        Car car3 = factoryBean2.getObject();

        System.out.println(car1);
//        System.out.println(car2);
        System.out.println(factoryBean == factoryBean2);
//        System.out.println(car1 == car2);
        System.out.println(car1 == car3);
    }
}
