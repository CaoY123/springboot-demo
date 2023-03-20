package com.mine.annotation.service;

import com.mine.annotation.dao.AccountDao;
import com.mine.annotation.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CaoYang
 * @create 2023-03-20-12:27 PM
 * @description
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    public Account findAccountById(String accountId) {
        return accountDao.findAccountById(accountId);
    }

}
