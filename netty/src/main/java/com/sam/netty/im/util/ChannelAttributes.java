package com.sam.netty.im.util;

import com.sam.netty.im.protocal.command.Session;
import io.netty.util.AttributeKey;

/**
 * 给channel添加属性
 * @author sam.liang
 */
public interface ChannelAttributes {
    /**
     * 登录成功标志
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
