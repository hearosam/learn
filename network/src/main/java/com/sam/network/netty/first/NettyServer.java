package com.sam.network.netty.first;

import com.sam.network.netty.first.handler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * netty server
 * @author sam.liang
 */
public class NettyServer {

    private static final int PORT = 12345;
    private EventLoopGroup main = new NioEventLoopGroup();
    private EventLoopGroup worker = new NioEventLoopGroup();
    private NettyServerHandler handler = new NettyServerHandler();

    public static void main(String[] args) throws InterruptedException {
        //启动netty server
        new NettyServer().start();
    }

    /**
     * 啟動netty服務端
     * @throws InterruptedException
     */
    private void start() throws InterruptedException {
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(main,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(handler);
                    }
                });
        ChannelFuture future = sb.bind(NettyServer.PORT).sync();
        future.channel().closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                main.shutdownGracefully();
                worker.shutdownGracefully();
            }
        });
    }

}
