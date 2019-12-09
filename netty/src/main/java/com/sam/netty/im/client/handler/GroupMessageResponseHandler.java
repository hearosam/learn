package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.GroupMessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 群组消息响应逻辑处理器
 * @author sam.liang
 */
public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket packet) throws Exception {
        System.out.println(packet.getUserName()+"-->"+packet.getMessage());
    }
}
