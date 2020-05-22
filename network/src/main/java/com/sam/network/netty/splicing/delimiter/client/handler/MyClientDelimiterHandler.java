package com.sam.network.netty.splicing.delimiter.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义分隔符handler
 * @author sam.liang
 */
public class MyClientDelimiterHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 计数器
     */
   private AtomicInteger count = new AtomicInteger();
    /**
     * 分隔符
     */
   private static final String DELIMITER = "\r\n";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "hello netty "+DELIMITER;
        for (int i = 0; i < 100; i++) {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(msg.getBytes(CharsetUtil.UTF_8)));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.out.println(msg.toString(CharsetUtil.UTF_8)+"  "+count.incrementAndGet());
    }
}
