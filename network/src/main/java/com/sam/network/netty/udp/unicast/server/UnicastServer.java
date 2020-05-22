package com.sam.network.netty.udp.unicast.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 单播演示示例
 * @author sam.liang
 */
public class UnicastServer {

    private static EventLoopGroup worker = new NioEventLoopGroup();
    private static UnicastServerHandler handler = new UnicastServerHandler();
    public static void main(String[] args) throws InterruptedException {
        Bootstrap server = new Bootstrap();
        server.group(worker)
                .channel(NioDatagramChannel.class)
                .handler(handler);
        ChannelFuture future = server.bind(12345).sync();
        System.out.println("应答端已启动。。。。");
        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                worker.shutdownGracefully();
            }
        });
    }
}
