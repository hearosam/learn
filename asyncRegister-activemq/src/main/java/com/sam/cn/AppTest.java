package com.sam.cn;

import com.sam.cn.controller.UserInfoController;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppTest {

    public static void main(String[] args) {
        /**
         * 相关消费者在activemq工程--->com.sam.mq.spring.consumer包下
         */
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ctx.start();
        UserInfoController controller = (UserInfoController) ctx.getBean("userInfoController");
        controller.userRegister();
        controller.userRegister2();
        controller.userRegister3();
        System.out.println("消息已经发送成功");
    }
}
