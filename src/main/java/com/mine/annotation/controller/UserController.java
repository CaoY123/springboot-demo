package com.mine.annotation.controller;

import com.mine.annotation.anno.SelfDefineLog;
import com.mine.annotation.entity.User;
import com.mine.annotation.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @SelfDefineLog("accountId")
    @RequestMapping("user/{id}")
    public User findUser(@PathVariable("id") Integer id) {
        return userService.findUserById(id);
    }

//    @SelfDefineLog("调用了compared方法")
//    @RequestMapping("compared")
//    public void comparedMethod() {}
}
