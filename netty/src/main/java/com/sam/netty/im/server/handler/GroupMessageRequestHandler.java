package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.packet.GroupMessageRequestPacket;
import com.sam.netty.im.protocal.packet.GroupMessageResponsePacket;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 群组消息请求数据包逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();
    protected GroupMessageRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket packet) throws Exception {
        String groupId = packet.getGroupId();
        //TODO 这里有一个bug就是不在群里面的用户也可以向群内发送消息
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        if(channelGroup!=null) {
            GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
            responsePacket.setMessage(packet.getMessage());
            responsePacket.setUserName(SessionUtil.getSession(ctx.channel()).getUserName());
            //发送消息到指定群组
            channelGroup.writeAndFlush(responsePacket);
        }
    }
}
