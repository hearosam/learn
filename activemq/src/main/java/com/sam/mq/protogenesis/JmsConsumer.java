package com.sam.mq.protogenesis;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 同步接受消息
 * @author sam.liang
 */
public class JmsConsumer {

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    private static final String BORKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    public static void main(String[] args) {
        /**
         * 连接工厂
         */
        ConnectionFactory connectionFactory;
        /**
         * 连接
         */
        Connection connection = null;
        /**
         * 会话
         */
        Session session;
        /**
         * 消息目的地
         */
        Destination destination;
        /**
         * 消息消费者
         */
        MessageConsumer messageConsumer;
        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BORKER_URL);
        try {
            connection = connectionFactory.createConnection();
            /**
             * 必须要启动连接
             */
            connection.start();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("HelloActiveMQ");
            MessageConsumer consumer = session.createConsumer(destination);
            Message receive = null;
            while ((receive = consumer.receive())!=null){
                TextMessage message = (TextMessage)receive;
                System.out.println("接受到消息："+message.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if (connection!=null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
