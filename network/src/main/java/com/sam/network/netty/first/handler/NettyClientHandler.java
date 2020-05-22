package com.sam.network.netty.first.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * netty client handler
 * @author sam.liang
 */
public class NettyClientHandler extends SimpleChannelInboundHandler {

    /**
     * 当接受到服务器的消息的时候这个方法将会被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("client receive data "+in.toString(CharsetUtil.UTF_8));
    }

    /**
     * 当channel被激活的时候会调用这个方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知channel是活跃的时候向服务端响应一条消息
        ByteBuf byteBuf = Unpooled.wrappedBuffer("hello netty!".getBytes(CharsetUtil.UTF_8));
        for (int i = 0; i < 100; i++) {
            ctx.writeAndFlush(byteBuf);
        }
    }

    /**
     * 发生异常的时候将会调用这个方法
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
