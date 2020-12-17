package com.wyfx.total.service;

import com.wyfx.total.entity.User;

public interface UserService {


    User findUserById(Integer uid);

    Boolean addUser(User user);

    User getLogin(String userName, String password);

    boolean updateUserName(User user);

    boolean updatePassword(User user);

    User findUserByUserName(String userName);
}
