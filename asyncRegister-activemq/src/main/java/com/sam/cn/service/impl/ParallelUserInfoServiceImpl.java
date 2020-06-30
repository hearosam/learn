package com.sam.cn.service.impl;

import com.sam.cn.entity.UserInfo;
import com.sam.cn.service.UserInfoService;
import org.springframework.stereotype.Service;

/**
 * 使用多线程并行处理用户注册
 * @author sam.liang
 */
@Service
public class ParallelUserInfoServiceImpl implements UserInfoService {


    @Override
    public String register(UserInfo userInfo) {
        System.out.println(Thread.currentThread().getName()+"save user info to db  username:"+userInfo.getName());
        new Thread(new SmsHandler(userInfo.getCellPhoneNo())).start();
        new Thread(new EmailHandle(userInfo.getEmail())).start();
        return "success";
    }

    private class SmsHandler implements Runnable{

        private String cellPhoneNo;
        public SmsHandler(String cellPhoneNo){
            this.cellPhoneNo = cellPhoneNo;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+ " send sms to user cellPhone : "+cellPhoneNo);
        }
    }
    /**
     * 邮件发送处理类
     */
    private class EmailHandle implements Runnable{

        private String email;
        public EmailHandle(String email) {
            this.email = email;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+ "send email to user email: " + email);
        }
    }
}
