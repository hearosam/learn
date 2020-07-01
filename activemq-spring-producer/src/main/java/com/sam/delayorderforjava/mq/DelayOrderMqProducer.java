package com.sam.delayorderforjava.mq;

import com.google.gson.Gson;
import com.sam.delayorderforjava.entity.OrderEntity;
import com.sam.delayorderforjava.service.IDelayOrder;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 延时订单消息队列实现
 * @author sam.liang
 */
@Component
public class DelayOrderMqProducer implements IDelayOrder {

    @Autowired
    private JmsTemplate jmsQueueTemplate;

    @Override
    public void orderDelay(OrderEntity order, long expireTime) {
        jmsQueueTemplate.send("delayOrderQueue",new MessageGenerator(order, (expireTime*1000)));
    }


    private static class MessageGenerator implements MessageCreator {

        private OrderEntity orderEntity;
        private long expireTime;

        public MessageGenerator(OrderEntity orderEntity,long expireTime) {
            this.orderEntity = orderEntity;
            this.expireTime = expireTime;
        }

        @Override
        public Message createMessage(Session session) throws JMSException {
            System.out.println("订单:"+orderEntity.getOrderNo()+",超时时长："+expireTime+",被推进了消息队列，详情："+orderEntity.toString());
            Gson gson = new Gson();
            String msg = gson.toJson(orderEntity);
            TextMessage textMessage = session.createTextMessage(msg);
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,expireTime);
            return textMessage;
        }
    }
}
