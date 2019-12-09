package com.sam.netty.im.util;

import com.sam.netty.im.protocal.command.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * session 工具类
 * @author sam.liang
 */
@Data
public class SessionUtil {
    /**
     *  用户-channel 映射表
     */
    private static final Map<String, Channel> userChannelMap= new ConcurrentHashMap<>();
    private static final Map<String, ChannelGroup> channelGroupMap= new ConcurrentHashMap<>();

    /**
     * 建立用户与通道的映射关系
     * @param session
     * @param channel
     */
    public static void bindSession(Session session, Channel channel) {
        userChannelMap.put(session.getUserId(),channel);
        channel.attr(ChannelAttributes.SESSION).set(session);
    }

    public static void addChannelGroup(String groupId,ChannelGroup channels) {
        channelGroupMap.put(groupId,channels);
    }
    /**
     * 删除用户-channel映射关系
     * @param channel
     */
    public static void unBindSession(Channel channel) {
        if(hasLogin(channel)) {
            Session session = getSession(channel);
            userChannelMap.remove(session.getUserId());
            channel.attr(ChannelAttributes.SESSION).set(null);
        }
    }
    public static ChannelGroup getChannelGroup(String groupId) {
        return channelGroupMap.get(groupId);
    }
    private static boolean hasLogin(Channel channel) {
        return channel.hasAttr(ChannelAttributes.SESSION);
    }

    public static Session getSession(Channel channel){
        return channel.attr(ChannelAttributes.SESSION).get();
    }
    public static Channel getChannel(String userId) {
        return userChannelMap.get(userId);
    }

}
