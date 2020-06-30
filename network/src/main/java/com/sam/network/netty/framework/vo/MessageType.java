package com.sam.network.netty.framework.vo;

/**
 * 消息类型枚举
 * @author sam.liang
 */
public enum MessageType {
    /**
     * 业务请求消息
     */
    SERVICE_REQ((byte) 0),
    /**
     * 业务响应消息
     */
    SERVICE_RESP((byte)1),
    /**
     * 业务one way
     */
    SERVICE_ONE_WAY((byte)2),
    /**
     * 握手请求消息
     */
    LOGIN_REQ((byte)3),
    /**
     * 握手响应消息
     */
    LOGIN_RESP((byte)4),
    /**
     * 心跳请求消息
     */
    HEART_REQ((byte)5),
    /**
     * 心跳响应消息
     */
    HEART_RESP((byte)6);

    private byte value;

    MessageType(byte b) {
        this.value = b;
    }
    public byte value(){
        return this.value;
    }
}
