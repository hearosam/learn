package com.sam.network.netty.first;

import com.sam.network.netty.first.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * netty client
 * @author sam.liang
 */
public class NettyClient {

    /**
     * 主机名称
     */
    private static final String HOST="127.0.0.1";
    /**
     * 端口号
     */
    private static final int PORT=12345;
    public static void main(String[] args) throws InterruptedException {
        new NettyClient().start();
    }

    /**
     * 客户端启动方法
     */
    private void start() throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientHandler());
        ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                worker.shutdownGracefully();
            }
        });
    }
}
