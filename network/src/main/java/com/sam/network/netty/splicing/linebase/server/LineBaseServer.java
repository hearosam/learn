package com.sam.network.netty.splicing.linebase.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

/**
 * 回车换行符 解决消息粘包半包问题
 * @author sam.liang
 */
public class LineBaseServer {

    private static EventLoopGroup main = new NioEventLoopGroup();
    private static EventLoopGroup worker = new NioEventLoopGroup();

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(main,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024*10));
                        ch.pipeline().addLast(new LineBaseServerHandler());
                    }
                });

        ChannelFuture future = sb.bind(new InetSocketAddress(12345)).sync();
        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                main.shutdownGracefully();
                worker.shutdownGracefully();
            }
        });
    }
}
