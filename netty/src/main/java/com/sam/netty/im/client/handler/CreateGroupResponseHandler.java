package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 创建群聊响应逻辑处理器
 * @author sam.liang
 */
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CreateGroupResponsePacket responsePacket) throws Exception {
        System.out.println("群创建成功，id为："+responsePacket.getGroupId());
        System.out.println("群里面有："+ responsePacket.getUserNameList());
    }
}
