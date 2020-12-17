package com.wyfx.total.service.impl;

import com.wyfx.total.entity.User;
import com.wyfx.total.exception.PasswordNotMatchException;
import com.wyfx.total.exception.UserNameNotMatchException;
import com.wyfx.total.mapper.UserMapper;
import com.wyfx.total.service.UserService;
import com.wyfx.total.utile.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hk269
 */
@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过uid查询用户信息
     *
     * @param uid
     * @return
     */
    @Override
    public User findUserById(Integer uid) {
        User user = userMapper.selectByPrimaryKey(uid);
        return user;
    }

    /**
     * 添加账号
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public Boolean addUser(User user) {
        Integer row = userMapper.insert(user);
        return row > 0;
    }

    /**
     * 登陆
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User getLogin(String userName, String password) {
        //查询用户信息
        User u = userMapper.selectByUserName(userName);
        if (u == null) {//用户名错误
            throw new UserNameNotMatchException("用户名错误");
        }
        String pwd = MD5Util.md5(password);
        if (pwd.equals(u.getPassword())) {
            return u;
        } else {//密码错
            throw new PasswordNotMatchException("密码错误");
        }
    }


    /**
     * 修改用户名
     *
     * @param user
     * @return
     */
    @Override
    public boolean updateUserName(User user) {
        logger.info("updateUserName()=" + user);
        Integer row = userMapper.updateByPrimaryKeySelective(user);
        return row >= 0;
    }

    /**
     * 修改密码
     *
     * @param user
     * @return
     */
    @Override
    @Transactional
    public boolean updatePassword(User user) {
        logger.info("已经执行修改密码");
        Integer row = userMapper.updateByPrimaryKeySelective(user);
        return row >= 1;
    }

    /**
     * 通过用户名查找用户
     *
     * @param userName
     * @return
     */
    @Override
    public User findUserByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }


}
