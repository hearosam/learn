package com.sam.network.netty.framework.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据包头部
 * @author sam.liang
 */
@Data
public class PacketHeader {
    private int crcCode = 0xabef0101;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 会话Id
     */
    private int sessionId;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 消息优先级
     */
    private byte priority;
    /**
     * 附件，头部信息提供扩展
     */
    private Map<String,Object> attachment = new HashMap<>();

    @Override
    public String toString() {
        return "PacketHeader{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionId='" + sessionId + '\'' +
                ", type='" + type + '\'' +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
