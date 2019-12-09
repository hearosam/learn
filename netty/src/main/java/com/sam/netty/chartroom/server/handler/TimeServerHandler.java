package com.sam.netty.chartroom.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 服务器时间handler
 * @author sam.liang
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf timeBuf = ctx.alloc().buffer();
        timeBuf.writeBytes(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).getBytes());
        ChannelFuture channelFuture = ctx.writeAndFlush(timeBuf);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                /**
                 * assert格式(断言)
                 * （1）assert [boolean 表达式]
                 *      如果[boolean表达式]为true，则程序继续执行。
                 *      如果为false，则程序抛出AssertionError，并终止执行。
                 * （2）assert[boolean 表达式 : 错误表达式 （日志）]
                 *      如果[boolean表达式]为true，则程序继续执行。
                 *      如果为false，则程序抛出java.lang.AssertionError，输出[错误信息]
                 */
                assert channelFuture == future;
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
