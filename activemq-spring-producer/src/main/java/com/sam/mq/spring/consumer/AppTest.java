package com.sam.mq.spring.consumer;

import com.sam.mq.spring.consumer.queue.QueueSender;
import com.sam.mq.spring.consumer.topic.TopicSender;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppTest {

    public static void main(String[] args) {
        /**
         * 相关消费者在activemq工程--->com.sam.mq.spring.consumer包下
         */
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ctx.start();
        //Queue message producer
//        String destination = "springMq";
//        QueueSender sender = (QueueSender) ctx.getBean("queueSender");
        //Topic message producer
        String destination ="springTopicMq";
        TopicSender topicSender = (TopicSender) ctx.getBean("topicSender");
        topicSender.sendMsg(destination,"hello i am spring message");
        System.out.println("消息已经发送成功");
    }
}
