package com.sam.netty.chartroom.server;

import com.sam.netty.chartroom.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 聊天室服务器端
 * @author sam.liang
 */
public class EchoServerApp {
    private final int PORT;

    public EchoServerApp(int port) {
        this.PORT = port;
    }

    /**
     * 服务器启动类
     */
    public void start() throws InterruptedException {
        EchoServerHandler handler = new EchoServerHandler();

        NioEventLoopGroup bossgroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workgroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossgroup,workgroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,100)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(handler);
                    }
                });
        //阻塞绑定端口
        ChannelFuture future = b.bind(this.PORT).sync();
        //优雅关闭channel 释放资源
        future.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                bossgroup.shutdownGracefully();
                workgroup.shutdownGracefully();
            }
        });
    }

    /**
     * 入口类
     * @param args
     */
    public static void main(String[] args) {
        try {
            new EchoServerApp(8080).start();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
