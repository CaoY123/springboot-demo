package com.mine.annotation.manager.impl;

import com.mine.annotation.dao.AccountLogBaseInfoMapper;
import com.mine.annotation.entity.AccountLogBaseInfoExample;
import com.mine.annotation.entity.AccountLogBaseInfo;
import com.mine.annotation.manager.AccountLogBaseInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author CaoYang
 * @create 2023-03-22-2:46 PM
 * @description
 */
@Repository
public class AccountLogBaseInfoManagerImpl implements AccountLogBaseInfoManager {

    @Autowired
    private AccountLogBaseInfoMapper accountLogBaseInfoMapper;

    @Override
    public int save(AccountLogBaseInfo AccountLogBaseInfo) {
        return accountLogBaseInfoMapper.insert(AccountLogBaseInfo);
    }

    @Override
    public int deleteById(String id) {
        return accountLogBaseInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateById(AccountLogBaseInfo AccountLogBaseInfo) {
        return accountLogBaseInfoMapper.updateByPrimaryKeySelective(AccountLogBaseInfo);
    }

    @Override
    public AccountLogBaseInfo queryById(String id) {
        return accountLogBaseInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AccountLogBaseInfo> queryByAccountId(String accountId) {
        AccountLogBaseInfoExample example = new AccountLogBaseInfoExample();
        example.createCriteria().andAccountIdEqualTo(accountId);
        return accountLogBaseInfoMapper.selectByExample(example);
    }
}
