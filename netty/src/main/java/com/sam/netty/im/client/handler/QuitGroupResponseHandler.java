package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.QuitGroupResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 退出群聊响应数据包逻辑处理器
 * @author sam.liang
 */
public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket packet) throws Exception {
        if (packet.isSuccess()) {
            System.out.println(packet.getUserId()+"退出了群聊【"+packet.getGroupId()+"】");
        }else{
            System.out.println(packet.getUserId()+"退出群聊【"+packet.getGroupId()+"】失败，失败原因是："+packet.getReason());
        }
    }
}
