package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.command.Session;
import com.sam.netty.im.protocal.packet.MessageRequestPacket;
import com.sam.netty.im.protocal.packet.MessageResponsePacket;
import com.sam.netty.im.util.LoginUtil;
import com.sam.netty.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 请求消息数据包逻辑处理器
 * @author sam.liang
 */
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();
    protected MessageRequestHandler(){}
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messagePacket) throws Exception {
        //获取当前通道绑定的用户session
        Session session = SessionUtil.getSession(ctx.channel());
        System.out.println("收到客户端消息:"+messagePacket.getMessage());
        //创建服务端响应消息
        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setFromUserId(session.getUserId());
        responsePacket.setFromUserName(session.getUserName());
        responsePacket.setMessage("【"+messagePacket.getMessage()+"】");

        String toUserId = messagePacket.getToUserId();
        Channel toUserChannel = SessionUtil.getChannel(toUserId);
        //写数据
        if(toUserChannel != null && LoginUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(responsePacket);
        }else{
            System.out.println("消息发送失败，用户:"+toUserId+"不在线");
        }
    }
}
