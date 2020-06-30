package com.sam.springdubbo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sam.springdubbo.entity.UserInfo;
import com.sam.springdubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 用户controller
 * @author sam.liang
 */
@Controller
public class UserController {

//    @Reference
    @Autowired
    private UserService userService;

    public List<UserInfo> fingUserList() {
        return userService.findUserList();
    }
}
