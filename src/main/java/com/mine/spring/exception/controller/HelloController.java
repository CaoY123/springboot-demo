package com.mine.spring.exception.controller;

import com.mine.spring.exception.domain.RetResult;
import com.mine.spring.exception.enums.RetCodeEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CaoYang
 * @create 2023-04-23-4:08 PM
 * @description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public RetResult<String> hello() {
        RetResult<String> result = new RetResult<>();
        result.setCode(RetCodeEnum.SUCCESS);
        result.setMessage("成功返回");
        result.setResult("你好啊，欢迎查看我的代码！");
        return result;
    }

    // 故意出错
    @GetMapping("/error")
    public void intentionalError() {
        int r = 3 / 0;
        RetResult<Integer> result = new RetResult<>();
        result.setCode(RetCodeEnum.SUCCESS);
        result.setMessage("运算成功");
        result.setResult(r);
    }
}
