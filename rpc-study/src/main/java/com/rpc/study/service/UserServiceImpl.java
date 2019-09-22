package com.rpc.study.service;

import com.rpc.study.api.UserService;
import com.rpc.study.entidy.User;

public class UserServiceImpl implements UserService {
    public String getUser() {
        User user = new User();
        user.setId("1");
        user.setName("hello");

        return "getId:" + user.getId() + ",getName:" + user.getName();
    }
}
