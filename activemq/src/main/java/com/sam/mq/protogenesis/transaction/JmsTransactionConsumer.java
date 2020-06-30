package com.sam.mq.protogenesis.transaction;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 手动确认消息
 * @author sam.liang
 */
public class JmsTransactionConsumer {

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
            /**
             * 1、	AUTO_ACKNOWLEDGE = 1    自动确认
             * 2、	CLIENT_ACKNOWLEDGE = 2    客户端手动确认
             * 3、	DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
             * 4、	SESSION_TRANSACTED = 0    事务提交并确认
             */
            session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
            destination = session.createQueue("HelloActiveMQ");
            MessageConsumer consumer = session.createConsumer(destination);
            Message receive = null;
            while ((receive = consumer.receive())!=null){
                TextMessage message = (TextMessage)receive;
                System.out.println("接受到消息："+message.getText());
                //手动确认
                message.acknowledge();
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
