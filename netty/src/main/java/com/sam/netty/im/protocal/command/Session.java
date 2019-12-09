package com.sam.netty.im.protocal.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户session工具类
 * @author sam.liang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private String userName;
    private String userId;
}
