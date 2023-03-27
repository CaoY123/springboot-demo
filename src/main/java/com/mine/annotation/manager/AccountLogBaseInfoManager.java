package com.mine.annotation.manager;


import com.mine.annotation.entity.AccountLogBaseInfo;

import java.util.List;

/**
 * @author CaoYang
 * @create 2023-03-22-2:45 PM
 * @description 账务日志信息管理类
 */
public interface AccountLogBaseInfoManager {
    int save(AccountLogBaseInfo accountLogBaseInfoWithBLOBs);
    int deleteById (String id);
    int updateById(AccountLogBaseInfo accountLogBaseInfo);
    AccountLogBaseInfo queryById(String id);
    List<AccountLogBaseInfo> queryByAccountId(String accountId);
}
