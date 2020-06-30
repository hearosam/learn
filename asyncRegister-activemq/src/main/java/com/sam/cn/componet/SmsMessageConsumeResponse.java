package com.sam.cn.componet;

import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 消息消费响应listener
 * @author sam.liang
 */
@Component
public class SmsMessageConsumeResponse implements MessageListener {

    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        try {
            System.out.println("收到SMS MQ消息消费响应："+msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
