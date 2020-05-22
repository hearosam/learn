package com.sam.network.netty.splicing.linebase.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.StandardCharsets;

/**
 *
 * @author sam.liang
 */
public class LineBaseClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当网卡缓冲区有可读数据的时候就会回调该方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println("receive from server"+msg.toString(StandardCharsets.UTF_8));
    }

    /**
     * 当通道成功连接的时候调用该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String request = "hello netty"+System.getProperty("line.separator");
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.wrappedBuffer(request.getBytes());
            ctx.writeAndFlush(byteBuf);
        }
    }

    /**
     * 当出现异常的时候会调用该方法
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
