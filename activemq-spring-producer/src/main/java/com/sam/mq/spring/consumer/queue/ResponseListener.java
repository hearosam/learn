package com.sam.mq.spring.consumer.queue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 监听消息被消费的响应消息
 * @author sam.liang
 */
@Component
public class ResponseListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        TextMessage msg = (TextMessage) message;
        try {
            System.out.println("收到消息被消费的响应消息："+ msg.getText());
            System.out.println("开始执行消息被消费后的处理逻辑");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
