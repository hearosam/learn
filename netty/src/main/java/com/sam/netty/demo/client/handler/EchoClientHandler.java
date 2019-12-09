package com.sam.netty.demo.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * netty echo 服务器 客户端
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler {

    /**
     * 在服务器的连接在被建立的时候将会调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知channel是活跃的时候，向服务端发送一条消息
        ByteBuf buf = Unpooled.wrappedBuffer("hello netty!".getBytes(CharsetUtil.UTF_8));
        /// ctx.writeAndFlush(Unpooled.copiedBuffer("hello netty!", CharsetUtil.UTF_8));
        ctx.writeAndFlush(buf);
    }

    /**
     * 当从服务器接收到一条消息的时候被调用
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf in = (ByteBuf) o;
        System.out.println("Client recive Data：" + in.toString(CharsetUtil.UTF_8));
    }

    /**
     * 在处理过程中引发异常的时候被调用
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
