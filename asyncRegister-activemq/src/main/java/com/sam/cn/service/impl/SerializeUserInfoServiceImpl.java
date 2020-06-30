package com.sam.cn.service.impl;

import com.sam.cn.entity.UserInfo;
import com.sam.cn.service.UserInfoService;
import org.springframework.stereotype.Service;


/**
 * 普通串行执行
 * @author sam.liang
 */
@Service
public class SerializeUserInfoServiceImpl implements UserInfoService {

    @Override
    public String register(UserInfo userInfo) {
        System.out.println("save user"+userInfo.getName()+" info to db ");
        System.out.println("send email to user email:"+userInfo.getEmail());
        System.out.println("send sms to user cellphone"+userInfo.getCellPhoneNo());
        return "success";
    }
}
