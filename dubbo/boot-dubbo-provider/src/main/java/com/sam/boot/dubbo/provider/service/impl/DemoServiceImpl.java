package com.sam.boot.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.sam.dubbo.demo.service.DemoService;

import java.util.ArrayList;
import java.util.List;

/**
 * dubbo服务提供者
 * .@Service注解用的是dubbo的然后将会自动将demoService服务注册到zk里面
 * @author sam.liang
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public List<String> getPermissions(int uid) {
        List<String> permission = new ArrayList<>();
        permission.add(String.format("Permission_%d", uid - 1));
        permission.add(String.format("Permission_%d", uid));
        permission.add(String.format("Permission_%d", uid + 1));
        return permission;
    }
}
