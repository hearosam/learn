package com.sam.delayorderforjava.service;

import com.sam.delayorderforjava.entity.OrderEntity;

public interface IDelayOrder {

    void orderDelay(OrderEntity order,long expireTime);
}
