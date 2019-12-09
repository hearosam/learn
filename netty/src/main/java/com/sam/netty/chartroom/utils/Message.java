package com.sam.netty.chartroom.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 消息数据负载
 * @author sam.liang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String username;
    private Date sentTime;
    private String msg;
}
