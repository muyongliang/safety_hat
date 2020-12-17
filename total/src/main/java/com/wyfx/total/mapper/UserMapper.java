package com.wyfx.total.mapper;

import com.wyfx.total.entity.User;
import org.springframework.stereotype.Repository;

@Repository//2019-11-7
public interface UserMapper {

    int deleteByPrimaryKey(Integer uid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer uid);

    User selectLogin(String uname, String password);

    User selectByUserName(String userName);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}