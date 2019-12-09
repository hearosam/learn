package com.sam.dubbo.demo.consumer;

import java.util.List;

/**
 * 服务消费者
 * @author sam.liang
 */
public interface DemoServiceConsumer {

    List<String> getAllPermissions(int uid);
}
