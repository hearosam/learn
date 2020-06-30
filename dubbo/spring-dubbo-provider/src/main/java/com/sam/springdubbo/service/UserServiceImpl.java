package com.sam.springdubbo.service;

import com.sam.springdubbo.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 用户接口实现类
 */
@Service
public class UserServiceImpl implements UserService {
    private static final List<UserInfo> userList = Arrays.asList(new UserInfo("zhangsan","guangzhou"),
            new UserInfo("lisi","beijing"),
            new UserInfo("wangwu","shanghai"));

    @Override
    public List<UserInfo> findUserList(){
        return userList;
    }
}
