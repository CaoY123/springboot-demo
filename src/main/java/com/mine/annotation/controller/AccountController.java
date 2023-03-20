package com.mine.annotation.controller;

import com.mine.annotation.anno.SelfDefineLog;
import com.mine.annotation.entity.Account;
import com.mine.annotation.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author CaoYang
 * @create 2023-03-20-12:20 PM
 * @description 账户控制器
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @SelfDefineLog("accountId")
    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable("accountId") String accountId) {
        return accountService.findAccountById(accountId);
    }

    @SelfDefineLog("accountId")
    @PostMapping
    public Account getAccount(@RequestBody Account account) {
        account.setAccountName("处理后的命名");
        return account;
    }
}
