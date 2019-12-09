package com.sam.netty.im.server.handler;

import com.sam.netty.im.protocal.packet.HeartBeatRequestPacket;
import com.sam.netty.im.protocal.packet.HeartBeatResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 服务端处理心跳数据包逻辑处理器
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();
    private HeartBeatRequestHandler(){}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket packet) throws Exception {
        System.out.println("收到心跳，正在响应心跳。。。");
        ctx.writeAndFlush(new HeartBeatResponsePacket());

    }
}
