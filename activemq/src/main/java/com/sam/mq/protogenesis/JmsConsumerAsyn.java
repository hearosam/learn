package com.sam.mq.protogenesis;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 异步消息消费者
 * @author sam.liang
 */
public class JmsConsumerAsyn {
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection;
        Session session;
        Destination destination;
        MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("HelloActiveMQ");
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage msg = (TextMessage) message;
                    try {
                        System.out.println("接收到消息："+msg.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
