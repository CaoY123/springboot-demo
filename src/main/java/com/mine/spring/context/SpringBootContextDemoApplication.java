package com.mine.spring.context;

import com.mine.spring.context.clazz.MyContextFirst;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author CaoYang
 * @create 2023-04-22-2:19 AM
 * @description 测试spring 上下文的boot启动类
 * 启动时需要排除数据库配置
 */
@SpringBootApplication
public class SpringBootContextDemoApplication {

    public static void main(String[] args) {
        // 获取spring应用上下文
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootContextDemoApplication.class, args);
        MyContextFirst myContextFirst = applicationContext.getBean(MyContextFirst.class);
        // 调用doSomething()，看看是否获取spring上下文生效
        myContextFirst.doSomething();
    }

}
