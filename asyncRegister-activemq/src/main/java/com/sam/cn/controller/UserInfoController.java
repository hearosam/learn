package com.sam.cn.controller;

import com.sam.cn.entity.UserInfo;
import com.sam.cn.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

/**
 * 用户信息维护
 * @author sam.liang
 */
@Controller
public class UserInfoController {

    @Autowired
    @Qualifier("serializeUserInfoServiceImpl")
    private UserInfoService service;
    @Autowired
    @Qualifier("parallelUserInfoServiceImpl")
    private UserInfoService service2;
    @Autowired
    @Qualifier("mqUserInfoServiceImpl")
    private UserInfoService service3;


    /**
     * 用户注册
     * @return 是否成功
     */
    public String userRegister() {
        return service.register(new UserInfo("zhangsan","123@163.com","123456"));
    }
    public String userRegister2() {
        return service2.register(new UserInfo("lisi","666@163.com","98766"));
    }
    public String userRegister3() {
        return service3.register(new UserInfo("wangwu","777@163.com","55555"));
    }
}
