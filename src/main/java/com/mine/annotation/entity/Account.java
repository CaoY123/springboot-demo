package com.mine.annotation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author CaoYang
 * @create 2023-03-20-12:16 PM
 * @description 账户类 —— 用于测试
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    // 账户号
    private String accountId;
    // 账户的用户名
    private String accountName;
    // 账户的金额
    private BigDecimal balance;
}
