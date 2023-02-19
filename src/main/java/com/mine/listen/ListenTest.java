package com.mine.listen;

import com.mine.listen.publisher.MyPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author CaoYang
 * @create 2023-02-19-1:10 PM
 * @description 监听机制测试类
 */
@SpringBootApplication
public class ListenTest {

    public static void main(String[] args) {
        // 事件发布器测试
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ListenTest.class, args);
        // 获取容器中的事件发布器
        MyPublisher publisher = applicationContext.getBean(MyPublisher.class);
        publisher.publishEvent();
    }

}
