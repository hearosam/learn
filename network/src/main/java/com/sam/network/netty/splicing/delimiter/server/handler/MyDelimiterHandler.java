package com.sam.network.netty.splicing.delimiter.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义分隔符handler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class MyDelimiterHandler extends ChannelInboundHandlerAdapter {
    /**
     * 计数器
     */
    private AtomicInteger count = new AtomicInteger();
    /**
     * 分隔符
     */
    private static final String DELIMITER = "\r\n";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf message = (ByteBuf)msg;
        String msgContent =message.toString(CharsetUtil.UTF_8);
        System.out.println("receive client message"+msgContent+" ------>count: "+count.incrementAndGet());
        String responseContent = msgContent+DELIMITER;
        ByteBuf byteBuf = Unpooled.wrappedBuffer(responseContent.getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
