package com.sam.springdubbo.service;

import com.sam.springdubbo.entity.UserInfo;
import java.util.List;

/**
 * 用户接口
 * @author sam.liang
 */
public interface UserService {
    List<UserInfo> findUserList();
}
