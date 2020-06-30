package com.sam.mq.protogenesis;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息提供者
 * queue 默认是进行持久化的
 * @author sam.liang
 */
public class JmsProducer {
    /**默认连接用户名*/
    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
    /**
     * 默认连接密码
     */
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
    /**
     * 默认连接地址
     */
    private static final String BROKER_URL = ActiveMQConnection.DEFAULT_BROKER_URL;

    /**
     * 发送次数
     */
    private static final int SENDNUM = 3;

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
         * 消息生产者
         */
        MessageProducer producer;
        connectionFactory = new ActiveMQConnectionFactory(USERNAME,PASSWORD,BROKER_URL);
        try {
            /**
             * 通过连接工厂获取连接
             */
            connection = connectionFactory.createConnection();
            /**
             * 启动连接
             */
            connection.start();;
            /**
             * 创建session,第一个参数表示是否使用事务，第二个参数表示是否自动确认
             */
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            /**
             * 创建一个名为HelloActiveMQ的消息队列
             */
            destination = session.createQueue("HelloActiveMQ");
            /**
             * 创建消息生产者
             */
            producer = session.createProducer(destination);
            for (int i = 0; i < SENDNUM; i++) {
                String msg = "发送消息："+i+" "+System.currentTimeMillis();
                TextMessage textMessage = session.createTextMessage(msg);
                producer.send(textMessage);
                System.out.println(msg);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally {
            if (connection != null ) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


