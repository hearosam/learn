package com.sam.mq.spring.consumer.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class QueueSender {

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResponseListener responseListener;

    public void sendMsg(String destination,String msg) {

        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage(msg);
                //使用请求应答模式，目的使得消息被消费之后告诉生产者消息已经被消费了
//                Destination destination = session.createTemporaryQueue();
                Destination destination = session.createQueue("tempqueue");
                MessageConsumer consumer = session.createConsumer(destination);
                message.setJMSMessageID(System.currentTimeMillis()+"");
                message.setJMSReplyTo(destination);
                consumer.setMessageListener(responseListener);
                return message;
            }
        });
    }
}
