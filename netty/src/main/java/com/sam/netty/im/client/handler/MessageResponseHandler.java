package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 消息响应逻辑处理器
 * @author sam.liang
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageResponsePacket responsePacket) throws Exception {
        System.out.println("收到"+responsePacket.getFromUserId()+"("+responsePacket.getFromUserName()+")消息："+responsePacket.getMessage());
    }
}
