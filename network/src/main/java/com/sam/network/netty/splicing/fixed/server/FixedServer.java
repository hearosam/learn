package com.sam.network.netty.splicing.fixed.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;

/**
 * 固定长度演示 服务端
 * @author sam.liang
 */
public class FixedServer {

    private static final EventLoopGroup main = new NioEventLoopGroup();
    private static final EventLoopGroup worker = new NioEventLoopGroup();

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap sb = new ServerBootstrap();
        FixedServerHandler fixedServerHandler = new FixedServerHandler();
        sb.group(main,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new FixedLengthFrameDecoder("afasdfadsfasdfadsasfasdfsdfaasdf".length()));
                        ch.pipeline().addLast(fixedServerHandler);
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
