package com.mine.annotation.controller;

import com.mine.annotation.anno.LogInfo;
import com.mine.annotation.anno.SelfDefineLog;
import com.mine.annotation.entity.Account;
import com.mine.annotation.entity.ModuleEnum;
import com.mine.annotation.entity.OperateEnum;
import com.mine.annotation.entity.RerunCondition;
import com.mine.annotation.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

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

//    @SelfDefineLog("accountId")
    @LogInfo(moduleName = ModuleEnum.RERUN, operateType = OperateEnum.UPDATE)
    @PostMapping
    public Account getAccount(@RequestBody Account account) {
        account.setAccountName("处理后的命名");
//        int r = exceptionFunc(4);
        return account;
    }

    @LogInfo(moduleName = ModuleEnum.RERUN, operateType = OperateEnum.INSERT)
    @PostMapping("/rerun")
    public Account rerun(@RequestBody RerunCondition condition) {
        System.out.println("传入的条件对象为：" + condition);
        Date date = new Date();
        long time = date.getTime();
        Account account = new Account(condition.getAccountId(), "user-" + time, new BigDecimal(time * 1.0 / 1000 - 56));
        return account;
    }

    private int exceptionFunc(int num) {
        int a = 3;
        return (num + a) / 0;
    }

    public static void main(String[] args) {
//        Timestamp timestamp = new Timestamp();
    }
}
