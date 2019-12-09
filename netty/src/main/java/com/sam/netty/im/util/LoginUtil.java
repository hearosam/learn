package com.sam.netty.im.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

/**
 * 登录工具类
 * @author sam.liang
 */
public class LoginUtil {

    /**
     * 标志当前渠道登录成功
     * @param channel
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(ChannelAttributes.LOGIN).set(true);
    }

    /**
     * 判断当前渠道是否登录
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        //获取渠道绑定的登录标识
        Attribute<Boolean> loginFlag = channel.attr(ChannelAttributes.LOGIN);
        return loginFlag.get() != null;
    }
}
