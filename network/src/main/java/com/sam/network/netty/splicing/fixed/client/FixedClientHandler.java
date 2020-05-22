package com.sam.network.netty.splicing.fixed.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * fixedClientHandler
 * @author sam.liang
 */
public class FixedClientHandler extends SimpleChannelInboundHandler {

    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 网卡缓冲区有数据可读的时候会调用该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        String response = byteBuf.toString(StandardCharsets.UTF_8);
        System.out.println("接收到服务端返回消息："+response + ",当前计数器值为："+counter.getAndIncrement());
    }

    /**
     * 成功建立连接的时候会调用该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.wrappedBuffer("afasdfadsfasdfadsasfasdfsdfaasdf".getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
