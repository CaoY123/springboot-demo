package com.mine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author CaoYang
 * @create 2023-03-16-9:59 AM
 * @description
 */
@EnableOpenApi
@SpringBootApplication
//@MapperScan({"com.mine.annotation.dao"})
public class SpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoApplication.class, args);
    }
}
