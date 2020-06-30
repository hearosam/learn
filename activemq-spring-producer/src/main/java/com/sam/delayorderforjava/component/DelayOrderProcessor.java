package com.sam.delayorderforjava.component;

import com.sam.delayorderforjava.entity.OrderEntity;
import org.springframework.stereotype.Component;

/**
 * 超时订单处理实现类
 */
@Component
public class DelayOrderProcessor {

    /**
     * 检查数据库中指定id的订单的状态，如果为未支付，
     * @param orderEntity
     */
    public void checkDelayOrder(OrderEntity orderEntity) {
        System.out.println("update payment set payStatus=超时支付 where id order='"+orderEntity.getOrderNo()+"' and payStatus=未支付");
        //如果影响行数>0说明已经将数据库的订单状态改成超时支付
        //如果影响行数=0说明不用更新数据库记录，订单已经支付了并未超时
        System.out.println("判断影响条数是否大于0,然后记录响应日志");
    }
}
