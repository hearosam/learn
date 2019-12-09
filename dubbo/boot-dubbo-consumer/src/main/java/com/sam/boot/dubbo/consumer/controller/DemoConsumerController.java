package com.sam.boot.dubbo.consumer.controller;

import com.sam.boot.dubbo.consumer.service.DemoConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 服务消费者测试controller
 * @author sam.liang
 */
@RestController
@RequestMapping("demo")
public class DemoConsumerController {

    @Autowired
    private DemoConsumerService service;

    @RequestMapping("/permissions")
    public List<String> getUserPermissions(@RequestParam int uid) {
        return service.getUserPermissions(uid);
    }
}
