package com.sam.netty.im.protocal.command;

/**
 * 指令类
 * @author sam.liang
 */
public interface Command {
    /**
     * 登录请求指令
     */
    Byte LOGIN_REQUEST = 1;
    /**
     * 登录响应指令
     */
    Byte LONGI_RESPONSE = 2;
    /**
     * 请求消息指令
     */
    Byte MESSAGE_REQUEST = 3;
    /**
     * 响应消息指令
     */
    Byte MESSAGE_RESPONSE = 4;
    /**
     * 创建群聊请求指令
     */
    Byte CREATE_GROUP_REQUEST = 5;
    /**
     * 创建群聊响应指令
     */
    Byte CREATE_GROUP_RESPONSE = 6;
    /**
     * 加入群聊请求指令
     */
    Byte JOIN_GROUP_REQUEST = 7;
    /**
     * 加入群聊效应指令
     */
    Byte JOIN_GROUP_RESPONSE = 8;
    /**
     * 退出群聊请求指令
     */
    Byte QUIT_GROUP_REQUEST = 9;
    /**
     * 退出群聊响应指令
     */
    Byte QUIT_GROUP_RESPONSE = 10;
    /**
     * 列出群聊所有用户请求指令
     */
    Byte LIST_GROUP_USER_REQUEST = 11;
    /**
     * 列出群聊所有用户响应指令
     */
    Byte LIST_GROUP_USER_RESPONSE = 12;
    /**
     * 群组消息发送指令
     */
    Byte GROUP_MESSAGE_REQUEST = 13;
    /**
     * 群组消息响应指令
     */
    Byte GROUP_MESSAGE_RESPONSE = 14;
    Byte HEARTBEAT_REQUEST = 15;
    Byte HEARTBEAT_RESPONSE = 16;
}
