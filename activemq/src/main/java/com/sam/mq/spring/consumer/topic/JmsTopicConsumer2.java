package com.sam.mq.spring.consumer.topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 这里使用同步获取消息
 * @author sam.liang
 */
public class JmsTopicConsumer2 {

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        MessageConsumer consumer;

        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKER_URL);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("springTopicMq");
            consumer = session.createConsumer(destination);
            Message receive = null;
            while((receive = consumer.receive())!=null){
                TextMessage message = (TextMessage) receive;
                System.out.println(message.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
