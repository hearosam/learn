package com.sam.boot.dubbo.consumer.service;

import java.util.List;

/**
 * 服务消费者接口
 * @author sam.liang
 */
public interface DemoConsumerService {
    /**
     * 获取用户权限
     * @param uid 用户id
     * @return
     */
    List<String> getUserPermissions(int uid);
}
