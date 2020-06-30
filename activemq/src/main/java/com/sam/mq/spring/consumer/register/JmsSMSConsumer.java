package com.sam.mq.spring.consumer.register;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 同步消费消息
 * @author sam.liang
 */
public class JmsSMSConsumer {

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
            destination = session.createQueue("user.register.sms");
            consumer = session.createConsumer(destination);
            Message receive = null;
            while((receive = consumer.receive())!=null){
                TextMessage msg = (TextMessage) receive;
                System.out.println("收到消息："+msg.getText()+",msgId："+msg.getJMSMessageID());
                Destination jmsReplyTo = msg.getJMSReplyTo();
                MessageProducer producer = session.createProducer(jmsReplyTo);
                TextMessage message = session.createTextMessage("消息已经被消费");
                producer.send(message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
