package com.sam.cn.service.impl;

import com.sam.cn.componet.SmsMessageConsumeResponse;
import com.sam.cn.entity.UserInfo;
import com.sam.cn.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * 使用消息队列并行处理用户注册业务
 * @author sam.liang
 */
@Service
public class MqUserInfoServiceImpl implements UserInfoService {

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    private SmsMessageConsumeResponse response;

    @Override
    public String register(UserInfo userInfo) {
        System.out.println("save user info to db :"+userInfo.getName());
        sendMessage("user.register.email",userInfo.getEmail(),false);
        sendMessage("user.register.sms",userInfo.getCellPhoneNo(),true);
        return "success";
    }

    private void sendMessage(String destination,String message,boolean isReplyTo){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                if (isReplyTo) {
                    Destination responseDestination = session.createTemporaryQueue();
                    MessageConsumer consumer = session.createConsumer(responseDestination);
                    textMessage.setJMSReplyTo(responseDestination);
                    textMessage.setJMSMessageID(System.currentTimeMillis()+"xixi");
                    consumer.setMessageListener(response);
                }
                return textMessage;
            }
        });
    }
}
