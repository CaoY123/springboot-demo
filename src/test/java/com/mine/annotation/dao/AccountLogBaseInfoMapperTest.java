package com.mine.annotation.dao;

import com.mine.annotation.dao.AccountLogBaseInfoMapper;
import com.mine.annotation.entity.AccountLogBaseInfo;
import com.mine.annotation.entity.AccountLogBaseInfoExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author CaoYang
 * @create 2023-03-27-11:35 AM
 * @description
 */
@SpringBootTest
public class AccountLogBaseInfoMapperTest {

    @Autowired
    private AccountLogBaseInfoMapper accountLogBaseInfoMapper;

    @Test
    public void testSelectOne() {

        AccountLogBaseInfo accountLogBaseInfo = accountLogBaseInfoMapper.selectByPrimaryKey("246b1a03-c42a-4ec5-8c03-259e6198cee5");
        System.out.println(accountLogBaseInfo);
    }

    @Test
    public void testSelectList() {
        AccountLogBaseInfoExample example = new AccountLogBaseInfoExample();
        AccountLogBaseInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCreateUserEqualTo("1679656203672");
        List<AccountLogBaseInfo> accountLogBaseInfos = accountLogBaseInfoMapper.selectByExample(example);
        accountLogBaseInfos.forEach(System.out::println);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        AccountLogBaseInfo info = new AccountLogBaseInfo();
        info.setId("246b1a03-c42a-4ec5-8c03-259e6198cee5");
        info.setOperateType("插入");
        int updateCount = accountLogBaseInfoMapper.updateByPrimaryKeySelective(info);
        AccountLogBaseInfo accountLogBaseInfo = accountLogBaseInfoMapper.selectByPrimaryKey("246b1a03-c42a-4ec5-8c03-259e6198cee5");
        System.out.println("更新的行数：" + updateCount);
        System.out.println(accountLogBaseInfo);
    }

    @Test
    public void testUpdateByExampleSelective() {
        AccountLogBaseInfoExample example = new AccountLogBaseInfoExample();
        example.createCriteria().andAccountIdEqualTo("042350");
        AccountLogBaseInfo info = new AccountLogBaseInfo();
        info.setOperateType("插入");
        int updateCount = accountLogBaseInfoMapper.updateByExampleSelective(info, example);
        System.out.println("更新记录数：" + updateCount);
        List<AccountLogBaseInfo> accountLogBaseInfos = accountLogBaseInfoMapper.selectByExample(example);
        accountLogBaseInfos.forEach(System.out::println);
    }

    @Test
    public void testDelete() {

        int deleteCount = accountLogBaseInfoMapper.deleteByPrimaryKey("01762ea6-343e-47c0-bbca-f41a72def639");
        System.out.println("删除的记录数：" + deleteCount);
    }

    @Test
    public void testCountByExample() {

        AccountLogBaseInfoExample example = new AccountLogBaseInfoExample();
        example.createCriteria().andAccountIdIsNotNull();
        long count = accountLogBaseInfoMapper.countByExample(example);
        System.out.println("accountId不为空的记录个数：" + count);
    }
}
