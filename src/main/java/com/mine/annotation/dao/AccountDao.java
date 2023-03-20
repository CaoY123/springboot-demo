package com.mine.annotation.dao;

import com.mine.annotation.entity.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author CaoYang
 * @create 2023-03-20-12:23 PM
 * @description
 */
@Component
public class AccountDao {

    public Account findAccountById(String accountId) {
        System.out.println("查询的accountId为：【" + accountId + "】");
        Account account = new Account(accountId, "account-" + accountId, new BigDecimal(1000));
        System.out.println("返回的账户为：【" + account + "】");
        return account;
    }
}
