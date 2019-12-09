package com.sam.dubbo.demo.service;

import java.util.List;

/**
 * @author sam.liang
 */
public interface DemoService {
    /**
     * 獲取用戶全新啊
     * @param uid 用戶id
     * @return
     */
    List<String> getPermissions(int uid);
}
