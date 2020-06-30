package com.sam.springdubbo;

import com.sam.springdubbo.controller.UserController;
import com.sam.springdubbo.entity.UserInfo;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class ConsumerStarter {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/dubbo.xml");
        context.start();
        UserController userController = (UserController)context.getBean("userController"); // 获取远程服务代理
        List<UserInfo> userInfos = userController.fingUserList();
        System.out.println( userInfos.size() ); // 显示调用结果
    }
}
