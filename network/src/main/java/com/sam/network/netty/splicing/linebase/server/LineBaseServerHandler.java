package com.sam.network.netty.splicing.linebase.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 回车换行解决粘包半包handler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class LineBaseServerHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 当网卡缓冲区有可读数据的时候将会调用这个方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String request = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println("接收到客户端数据："+request+",当前的计数器值为："+counter.getAndIncrement());
        String response = "response --> "+request+System.getProperty("line.separator");
        ctx.writeAndFlush(Unpooled.wrappedBuffer(response.getBytes()));
    }

    /**
     * 发生异常会将会调用这个方法
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
