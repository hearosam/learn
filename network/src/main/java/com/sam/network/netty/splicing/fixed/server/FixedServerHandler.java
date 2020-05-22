package com.sam.network.netty.splicing.fixed.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FixedServerHandler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class FixedServerHandler  extends ChannelInboundHandlerAdapter {
    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 服务端有可读网络数据的时候会调用该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String request = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println("服务器接收到客户端请求数据："+request+",并且当前的计数器值为："+counter.getAndIncrement());
    }

    /**
     * 发生异常的时候会调用该方法
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
