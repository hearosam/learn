package com.sam.boot.dubbo.consumer.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sam.boot.dubbo.consumer.service.DemoConsumerService;
import com.sam.dubbo.demo.service.DemoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务消费者接口实现
 * @author sam.liang
 */
@Service
public class DemoConsumerServiceImpl implements DemoConsumerService {

    @Reference
    private DemoService demoService;
    /**
     * @param uid 用户id
     * @return
     */
    @Override
    public List<String> getUserPermissions(int uid) {
        return demoService.getPermissions(uid);
    }
}
