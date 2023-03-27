package com.mine.annotation.dao;

import com.mine.annotation.entity.AccountLogBaseInfo;
import com.mine.annotation.entity.AccountLogBaseInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountLogBaseInfoMapper {
    long countByExample(AccountLogBaseInfoExample example);

    int deleteByExample(AccountLogBaseInfoExample example);

    int deleteByPrimaryKey(String id);

    int insert(AccountLogBaseInfo record);

    int insertSelective(AccountLogBaseInfo record);

    List<AccountLogBaseInfo> selectByExample(AccountLogBaseInfoExample example);

    AccountLogBaseInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AccountLogBaseInfo record, @Param("example") AccountLogBaseInfoExample example);

    int updateByExample(@Param("record") AccountLogBaseInfo record, @Param("example") AccountLogBaseInfoExample example);

    int updateByPrimaryKeySelective(AccountLogBaseInfo record);

    int updateByPrimaryKey(AccountLogBaseInfo record);
}