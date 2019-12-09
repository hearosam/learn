package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.ListGroupUserResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 列出群聊所有用户响应数据包逻辑处理器
 * @author sam.liang
 */
public class ListGroupUserResponseHandler extends SimpleChannelInboundHandler<ListGroupUserResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupUserResponsePacket packet) throws Exception {
        if(packet.isSuccess()) {
            System.out.println("群【"+packet.getGroupId()+"】包括人数："+packet.getUserNameList());
        }else{
            System.out.println("获取群聊人数失败，失败原因："+packet.getReason());
        }
    }
}
