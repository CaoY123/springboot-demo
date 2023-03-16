package com.mine.annotation.service;

import com.mine.annotation.dao.UserDao;
import com.mine.annotation.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findUserById(Integer id) {
        return userDao.findUserById(id);
    }
}

