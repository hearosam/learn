package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 加入群聊响应数据包逻辑处理器
 * @author sam.liang
 */
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket packet) throws Exception {
        if(packet.isSuccess()) {
            System.out.println("欢迎【"+packet.getUserId()+" 】加入【"+packet.getGroupId()+"】群");
        }else {
            System.out.println("【"+packet.getUserId()+" 】加入【"+packet.getGroupId()+"】群失败，失败原因是："+packet.getReason());
        }
    }
}
