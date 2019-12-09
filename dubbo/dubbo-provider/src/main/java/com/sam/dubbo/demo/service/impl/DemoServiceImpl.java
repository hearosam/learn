package com.sam.dubbo.demo.service.impl;

import com.sam.dubbo.demo.service.DemoService;

import java.util.ArrayList;
import java.util.List;

/**
 * 服務提供者
 * @author sam.liang
 */
public class DemoServiceImpl implements DemoService {

    /**
     * 獲取用戶所有權限列表
     * @param uid 用戶id
     * @return
     */
    @Override
    public List<String> getPermissions(int uid) {
        List<String> permission = new ArrayList<>();
        permission.add(String.format("Permission_%d", uid - 1));
        permission.add(String.format("Permission_%d", uid));
        permission.add(String.format("Permission_%d", uid + 1));
        return permission;
    }
}
