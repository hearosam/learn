package com.sam.mq.protogenesis;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 同步接受消息
 * @author sam.liang
 */
public class JmsTopicConsumer {

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
            //topic消息持久化需要设置clientid
            connection.setClientID("testClientId");
            connection.start();
            session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
//            destination = session.createTopic("HelloActiveTopicMQ");
//            MessageConsumer consumer = session.createConsumer(destination);
            //topic消息持久化destination要用Topic类型接收
            Topic topicMQ = session.createTopic("HelloActiveTopicMQ");
            TopicSubscriber subscriberName = session.createDurableSubscriber(topicMQ, "subscriberName");
            Message receive = null;
            while ((receive = subscriberName.receive())!=null){
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
