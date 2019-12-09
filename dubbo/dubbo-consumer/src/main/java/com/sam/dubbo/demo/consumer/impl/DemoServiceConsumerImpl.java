package com.sam.dubbo.demo.consumer.impl;

import com.sam.dubbo.demo.consumer.DemoServiceConsumer;
import com.sam.dubbo.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务消费则
 * @author sam.liang
 */
@Service
public class DemoServiceConsumerImpl implements DemoServiceConsumer {

    @Autowired
    private DemoService demoService;

    @Override
    public List<String> getAllPermissions(int uid) {
        return demoService.getPermissions(uid);
    }
}
