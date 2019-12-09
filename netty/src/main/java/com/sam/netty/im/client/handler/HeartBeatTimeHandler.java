package com.sam.netty.im.client.handler;

import com.sam.netty.im.protocal.packet.HeartBeatRequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * 客户端心跳handler
 * @author sam.liang
 */
public class HeartBeatTimeHandler extends ChannelInboundHandlerAdapter {

    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当客户端跟服务端建立连接立即发送心跳数据包
        scheduleSendHeartBeat(ctx);
        super.channelActive(ctx);
    }
    /**
     * 定时发送心跳数据包
     * @param ctx
     */
    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(()->{
            if(ctx.channel().isActive()) {
                System.out.println("发送心跳。。");
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                //无限递归发送心跳
                scheduleSendHeartBeat(ctx);
            }
        },HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
