package com.sam.network.netty.udp.broadcast.client;

import com.sam.network.netty.udp.broadcast.common.LogMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 广播接收端 handler
 */
public class BroadcastClientHandler extends SimpleChannelInboundHandler<LogMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogMsg msg) throws Exception {
        String sb = "[msgId:" + msg.getMsgId() + "," +
                "time:" + msg.getTime() + "," +
                "msg:" + msg.getMsg() + "]";
        System.out.println(sb);
    }
}
